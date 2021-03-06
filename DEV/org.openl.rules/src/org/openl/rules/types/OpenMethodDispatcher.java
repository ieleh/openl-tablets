package org.openl.rules.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

import org.openl.binding.MethodUtil;
import org.openl.binding.exception.DuplicatedMethodException;
import org.openl.exception.OpenLRuntimeException;
import org.openl.rules.context.IRulesRuntimeContextMutableUUID;
import org.openl.rules.context.RulesRuntimeContextFactory;
import org.openl.rules.lang.xls.binding.TableVersionComparator;
import org.openl.rules.lang.xls.binding.wrapper.IOpenMethodWrapper;
import org.openl.rules.lang.xls.prebind.LazyMethodWrapper;
import org.openl.rules.lang.xls.syntax.TableSyntaxNode;
import org.openl.rules.method.ITablePropertiesMethod;
import org.openl.rules.method.TableUriMethod;
import org.openl.rules.table.properties.DimensionPropertiesMethodKey;
import org.openl.runtime.IRuntimeContext;
import org.openl.syntax.exception.SyntaxNodeException;
import org.openl.syntax.exception.SyntaxNodeExceptionUtils;
import org.openl.types.*;
import org.openl.types.impl.MethodDelegator;
import org.openl.types.impl.MethodKey;
import org.openl.vm.IRuntimeEnv;
import org.openl.vm.Tracer;

/**
 * Class that decorates the <code>IOpenMehtod</code> interface for method
 * overload support.
 * 
 * @author Alexey Gamanovich
 * 
 */
public abstract class OpenMethodDispatcher implements IOpenMethod {

    /**
     * Delegate method. Used as a descriptor of method for all overloaded
     * version to delegate requests about method info such as signature, name,
     * etc.
     */
    private IOpenMethod delegate;

    /**
     * Method key. Used for method signatures comparison.
     */
    private MethodKey delegateKey;

    /**
     * List of method candidates.
     */
    private List<IOpenMethod> candidates = new ArrayList<IOpenMethod>();
    private final Invokable invokeInner = new Invokable() {
        @Override public Object invoke(Object target, Object[] params, IRuntimeEnv env) {
            return invokeInner(target, params, env);
        }
    };


    /**
     * Creates new instance of decorator.
     * 
     * @param delegate method to decorate
     */
    protected void decorate(IOpenMethod delegate) {

        // Check that IOpenMethod object is not null.
        //
        if (delegate == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }

        // Save method as delegate. It used by decorator to delegate requests
        // about method info such as signature, name, etc.
        //
        this.delegate = delegate;

        // Evaluate method key.
        //
        this.delegateKey = new MethodKey(delegate);

        // First method candidate is himself.
        //
        this.candidates.add(delegate);
    }

    /**
     * Gets the signature of method.
     */
    public IMethodSignature getSignature() {
        return delegate.getSignature();
    }

    /**
     * Gets the declaring class.
     */
    public IOpenClass getDeclaringClass() {
        return delegate.getDeclaringClass();
    }

    /**
     * Gets <code>null</code>. The decorator hasn't info about overloaded
     * methods.
     */
    public IMemberMetaInfo getInfo() {
        return null;
    }

    /**
     * Gets the type of method.
     */
    public IOpenClass getType() {
        return delegate.getType();
    }

    public boolean isStatic() {
        return delegate.isStatic();
    }

    /**
     * Gets the user-friendly name.
     */
    public String getDisplayName(int mode) {
        return delegate.getDisplayName(mode);
    }

    /**
     * Gets the method name.
     */
    public String getName() {
        return delegate.getName();
    }

    /**
     * Gets <code>this</code>. The decorator can't resolve which overloaded
     * method should be returned.
     */
    public IOpenMethod getMethod() {
        return this;
    }

    public List<IOpenMethod> getCandidates() {
        return candidates;
    }

    private Map<UUID, IOpenMethod> cache = new ConcurrentSkipListMap<UUID, IOpenMethod>();
    
    private static final int MAX_ELEMENTS_IN_CAHCE = 1000;
    private static final int MIN_ELEMENTS_IN_CAHCE = 800;

    /**
     * Invokes appropriate method using runtime context.
     */
    public Object invoke(Object target, Object[] params, IRuntimeEnv env) {
        return Tracer.invoke(invokeInner,target, params, env, this);
    }

    /**
     * Invokes appropriate method using runtime context.
     */
    protected Object invokeInner(Object target, Object[] params, IRuntimeEnv env) {

        // Gets the runtime context.
        //
        IRuntimeContext context = env.getContext();

        if (context == null) {
            // Using empty context: all methods will be matched by properties.
            context = RulesRuntimeContextFactory.buildRulesRuntimeContext();
        }

        // Get matching method.
        //
        IOpenMethod method;
        
        if (context instanceof IRulesRuntimeContextMutableUUID){
            IRulesRuntimeContextMutableUUID contextMutableUUID = (IRulesRuntimeContextMutableUUID) context;
            method = cache.get(contextMutableUUID.getUUID());
            if (method == null){
                method = findMatchingMethod(candidates, context);
                cache.put(contextMutableUUID.getUUID(), method);
                if (cache.size() > MAX_ELEMENTS_IN_CAHCE){
                    synchronized (cache) {
                        if (cache.size() > MAX_ELEMENTS_IN_CAHCE){
                            Iterator<UUID> itr = cache.keySet().iterator();
                            while (cache.size() > MIN_ELEMENTS_IN_CAHCE && itr.hasNext()){
                                cache.remove(itr.next());
                            }
                        }
                    }
                }
            }
        }else{
            method = findMatchingMethod(candidates, context);
        }
        Tracer.put(this, "rule", method);

        // Check that founded required method.
        //
        if (method == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Method signature: ");
            MethodUtil.printMethod(this, sb);
            sb.append("\n");
            sb.append("Context: ");
            sb.append(context.toString());

            String message = String.format("Appropriate overloaded method for '%1$s' not found. Details: \n%2$s",
                getName(),
                sb.toString());

            throw new OpenLRuntimeException(message);
        }

        while (method instanceof LazyMethodWrapper || method instanceof MethodDelegator) {
            if (method instanceof LazyMethodWrapper) {
                method = ((LazyMethodWrapper) method).getCompiledMethod(env);
            }
            if (method instanceof MethodDelegator) {
                MethodDelegator methodDelegator = (MethodDelegator) method;
                method = methodDelegator.getMethod();
            }
        }
        
        if (method instanceof IOpenMethodWrapper){
            method = ((IOpenMethodWrapper) method).getDelegate();
        }
        
        return method.invoke(target, params, env);
    }

    /**
     * In case we have several versions of one table we should add only the
     * newest or active version of table.
     * 
     * @param newMethod The methods that we are trying to add.
     * @param key Method key of these methods based on signature.
     * @param existedMethod The existing method.
     */
    protected IOpenMethod useActiveOrNewerVersion(IOpenMethod existedMethod, IOpenMethod newMethod, MethodKey key) throws DuplicatedMethodException {
        int compareResult = TableVersionComparator.getInstance().compare(existedMethod, newMethod);
        if (compareResult > 0) {
            return newMethod;
        } else if (compareResult == 0) {
            /**
             * Throw the error with the right message for the case
             * when the methods are equal
             */
            if (newMethod instanceof TableUriMethod && existedMethod instanceof TableUriMethod) {
                String newMethodHashUrl = ((TableUriMethod) newMethod).getTableUri();
                String existedMethodHashUrl = ((TableUriMethod) existedMethod).getTableUri();

                if (!newMethodHashUrl.equals(existedMethodHashUrl)) {
                    String message = ValidationMessages.getDuplicatedMethodMessage(existedMethod, newMethod);
                    throw new DuplicatedMethodException(message, existedMethod);
                }
            } else {
                throw new IllegalStateException("Implementation supports only TableUriMethod!");
            }
        }
        return existedMethod;
    }

    private int searchTheSameMethod(IOpenMethod candidate) {
        int i = 0;
        for (IOpenMethod existedMethod : candidates) {
            if (existedMethod instanceof ITablePropertiesMethod && candidate instanceof ITablePropertiesMethod) {
                DimensionPropertiesMethodKey existedMethodPropertiesKey = new DimensionPropertiesMethodKey(existedMethod);
                DimensionPropertiesMethodKey newMethodPropertiesKey = new DimensionPropertiesMethodKey(candidate);
                if (newMethodPropertiesKey.equals(existedMethodPropertiesKey)) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }
    
    private Set<MethodKey> candidateKeys = new HashSet<MethodKey>();

    /**
     * Try to add method as overloaded version of decorated method.
     * 
     * @param candidate method to add
     */
    public void addMethod(IOpenMethod candidate) {

        // Evaluate the candidate method key.
        //

        MethodKey candidateKey = new MethodKey(candidate);

        // Check that candidate has the same method signature and list of
        // parameters as a delegate. If they different then is two different
        // methods and delegate cannot be overloaded by candidate.
        //
        if (delegateKey.equals(candidateKey)) {
			int i = searchTheSameMethod(candidate);
			if (i < 0) {
				candidates.add(candidate);
			} else {
				IOpenMethod existedMethod = candidates.get(i);
				try {
					candidate = useActiveOrNewerVersion(existedMethod,
							candidate, candidateKey);
					candidates.set(i, candidate);
				} catch (DuplicatedMethodException e) {
					if (!candidateKeys.contains(candidateKey)){
						if (existedMethod instanceof IMemberMetaInfo) {
							IMemberMetaInfo memberMetaInfo = (IMemberMetaInfo) existedMethod;
							if (memberMetaInfo.getSyntaxNode() != null) {
								if (memberMetaInfo.getSyntaxNode() instanceof TableSyntaxNode) {
									SyntaxNodeException error = SyntaxNodeExceptionUtils.createError(e
                                                    .getMessage(), e,
                                            memberMetaInfo.getSyntaxNode());
									((TableSyntaxNode) memberMetaInfo
											.getSyntaxNode())
											.addError(error);
								}
							}
						}
						candidateKeys.add(candidateKey);
					}
					throw e;
				}
			}
        } else {
            // Throw appropriate exception.
            //
            StringBuilder sb = new StringBuilder();
            MethodUtil.printMethod(this, sb);

            throw new OpenLRuntimeException("Invalid method signature to overload: " + sb.toString());
        }
    }

    /**
     * Resolve best matching method to invoke.
     * 
     * @param candidates list of candidates
     * @param context runtime context
     * @return method to invoke
     */
    protected abstract IOpenMethod findMatchingMethod(List<IOpenMethod> candidates, IRuntimeContext context);

    public IOpenMethod getTargetMethod() {
        return this.delegate;
    }

    public abstract TableSyntaxNode getDispatcherTable();
}