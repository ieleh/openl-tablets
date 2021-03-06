package org.openl.rules.ruleservice.logging.cassandra;

import java.util.Date;

import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.DataType.Name;

@Table
public class LoggingRecord {
    @PrimaryKey
    private String id;

    private Date incomingTime;
    private Date outcomingTime;

    private String request;
    private String response;

    private String serviceName;
    private String url;
    private String inputName;
    private String publisherType;

    private String stringValue1;
    private String stringValue2;
    private String stringValue3;
    private String stringValue4;
    private String stringValue5;

    private Long numberValue1;
    private Long numberValue2;
    private Long numberValue3;
    private Long numberValue4;
    private Long numberValue5;

    private Date dateValue1;
    private Date dateValue2;
    private Date dateValue3;

    public LoggingRecord(String id,
            Date incomingTime,
            Date outcomingTime,
            String request,
            String response,
            String serviceName,
            String url,
            String inputName,
            String publisherType,
            String stringValue1,
            String stringValue2,
            String stringValue3,
            String stringValue4,
            String stringValue5,
            Long numberValue1,
            Long numberValue2,
            Long numberValue3,
            Long numberValue4,
            Long numberValue5,
            Date dateValue1,
            Date dateValue2,
            Date dateValue3) {
        super();
        this.id = id;
        this.incomingTime = incomingTime;
        this.outcomingTime = outcomingTime;
        this.request = request;
        this.response = response;
        this.serviceName = serviceName;
        this.url = url;
        this.inputName = inputName;
        this.publisherType = publisherType;
        this.stringValue1 = stringValue1;
        this.stringValue2 = stringValue2;
        this.stringValue3 = stringValue3;
        this.stringValue4 = stringValue4;
        this.stringValue5 = stringValue5;
        this.numberValue1 = numberValue1;
        this.numberValue2 = numberValue2;
        this.numberValue3 = numberValue3;
        this.numberValue4 = numberValue4;
        this.numberValue5 = numberValue5;
        this.dateValue1 = dateValue1;
        this.dateValue2 = dateValue2;
        this.dateValue3 = dateValue3;
    }

    public String getPublisherType() {
        return publisherType;
    }

    public String getId() {
        return id;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public Date getIncomingTime() {
        return incomingTime;
    }

    public Date getOutcomingTime() {
        return outcomingTime;
    }

    public String getInputName() {
        return inputName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUrl() {
        return url;
    }

    public String getStringValue1() {
        return stringValue1;
    }

    public String getStringValue2() {
        return stringValue2;
    }

    public String getStringValue3() {
        return stringValue3;
    }

    public String getStringValue4() {
        return stringValue4;
    }

    public String getStringValue5() {
        return stringValue5;
    }

    @CassandraType(type = Name.BIGINT)
    public Long getNumberValue1() {
        return numberValue1;
    }

    @CassandraType(type = Name.BIGINT)
    public Long getNumberValue2() {
        return numberValue2;
    }

    @CassandraType(type = Name.BIGINT)
    public Long getNumberValue3() {
        return numberValue3;
    }

    @CassandraType(type = Name.BIGINT)
    public Long getNumberValue4() {
        return numberValue4;
    }

    @CassandraType(type = Name.BIGINT)
    public Long getNumberValue5() {
        return numberValue5;
    }

    public Date getDateValue1() {
        return dateValue1;
    }

    public Date getDateValue2() {
        return dateValue2;
    }

    public Date getDateValue3() {
        return dateValue3;
    }

    @Override
    public String toString() {
        return "LoggingRecord [id=" + id + "]";
    }

    public static class LoggingRecordBuilder {
        private String id;

        private Date incomingTime;
        private Date outcomingTime;

        private String request;
        private String response;

        private String serviceName;
        private String url;
        private String inputName;
        private String publisherType;

        private String stringValue1;
        private String stringValue2;
        private String stringValue3;
        private String stringValue4;
        private String stringValue5;

        private Long numberValue1;
        private Long numberValue2;
        private Long numberValue3;
        private Long numberValue4;
        private Long numberValue5;

        private Date dateValue1;
        private Date dateValue2;
        private Date dateValue3;

        public LoggingRecord build() {
            return new LoggingRecord(id,
                incomingTime,
                outcomingTime,
                request,
                response,
                serviceName,
                url,
                inputName,
                publisherType,
                stringValue1,
                stringValue2,
                stringValue3,
                stringValue4,
                stringValue5,
                numberValue1,
                numberValue2,
                numberValue3,
                numberValue4,
                numberValue5,
                dateValue1,
                dateValue2,
                dateValue3);
        }

        public LoggingRecordBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public LoggingRecordBuilder setIncomingTime(Date incomingTime) {
            this.incomingTime = incomingTime;
            return this;
        }

        public LoggingRecordBuilder setOutcomingTime(Date outcomingTime) {
            this.outcomingTime = outcomingTime;
            return this;
        }

        public LoggingRecordBuilder setRequest(String request) {
            this.request = request;
            return this;
        }

        public LoggingRecordBuilder setResponse(String response) {
            this.response = response;
            return this;
        }

        public LoggingRecordBuilder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public LoggingRecordBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public LoggingRecordBuilder setInputName(String inputName) {
            this.inputName = inputName;
            return this;
        }

        public LoggingRecordBuilder setPublisherType(String publisherType) {
            this.publisherType = publisherType;
            return this;
        }

        public LoggingRecordBuilder setStringValue1(String stringValue1) {
            this.stringValue1 = stringValue1;
            return this;
        }

        public LoggingRecordBuilder setStringValue2(String stringValue2) {
            this.stringValue2 = stringValue2;
            return this;
        }

        public LoggingRecordBuilder setStringValue3(String stringValue3) {
            this.stringValue3 = stringValue3;
            return this;
        }

        public LoggingRecordBuilder setStringValue4(String stringValue4) {
            this.stringValue4 = stringValue4;
            return this;
        }

        public LoggingRecordBuilder setStringValue5(String stringValue5) {
            this.stringValue5 = stringValue5;
            return this;
        }

        public LoggingRecordBuilder setNumberValue1(Long numberValue1) {
            this.numberValue1 = numberValue1;
            return this;
        }

        public LoggingRecordBuilder setNumberValue2(Long numberValue2) {
            this.numberValue2 = numberValue2;
            return this;
        }

        public LoggingRecordBuilder setNumberValue3(Long numberValue3) {
            this.numberValue3 = numberValue3;
            return this;
        }

        public LoggingRecordBuilder setNumberValue4(Long numberValue4) {
            this.numberValue4 = numberValue4;
            return this;
        }

        public LoggingRecordBuilder setNumberValue5(Long numberValue5) {
            this.numberValue5 = numberValue5;
            return this;
        }

        public LoggingRecordBuilder setDateValue1(Date dateValue1) {
            this.dateValue1 = dateValue1;
            return this;
        }

        public LoggingRecordBuilder setDateValue2(Date dateValue2) {
            this.dateValue2 = dateValue2;
            return this;
        }

        public String getId() {
            return id;
        }

        public Date getIncomingTime() {
            return incomingTime;
        }

        public Date getOutcomingTime() {
            return outcomingTime;
        }

        public String getRequest() {
            return request;
        }

        public String getResponse() {
            return response;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getUrl() {
            return url;
        }

        public String getInputName() {
            return inputName;
        }

        public String getPublisherType() {
            return publisherType;
        }

        public String getStringValue1() {
            return stringValue1;
        }

        public String getStringValue2() {
            return stringValue2;
        }

        public String getStringValue3() {
            return stringValue3;
        }

        public String getStringValue4() {
            return stringValue4;
        }

        public String getStringValue5() {
            return stringValue5;
        }

        public Long getNumberValue1() {
            return numberValue1;
        }

        public Long getNumberValue2() {
            return numberValue2;
        }

        public Long getNumberValue3() {
            return numberValue3;
        }

        public Long getNumberValue4() {
            return numberValue4;
        }

        public Long getNumberValue5() {
            return numberValue5;
        }

        public Date getDateValue1() {
            return dateValue1;
        }

        public Date getDateValue2() {
            return dateValue2;
        }

        public Date getDateValue3() {
            return dateValue3;
        }

        public LoggingRecordBuilder setDateValue3(Date dateValue3) {
            this.dateValue3 = dateValue3;
            return this;
        }
    }
}
