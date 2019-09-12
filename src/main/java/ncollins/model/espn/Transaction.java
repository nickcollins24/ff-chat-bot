package ncollins.model.espn;

//    {
//        "author": "{3F579493-D60E-4AAD-9794-93D60E6AAD34}",
//        "date": 1567960327011,
//        "messages": [
//              {
//                  "author": "{3F579493-D60E-4AAD-9794-93D60E6AAD34}",
//                  "messageTypeId": 179,
//                  "targetId": 10475,
//                  "to": 5
//              },
//              {
//                  "author": "{3F579493-D60E-4AAD-9794-93D60E6AAD34}",
//                  "messageTypeId": 178,
//                  "targetId": -16007,
//                  "to": 5
//              }
//          ]
//    }
public class Transaction {
    private String author;
    private Message[] messages;
    private long date;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public class Message {
        private String author;
        private Integer messageTypeId;
        private Integer targetId;
        private Integer from;
        private Integer to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Integer getMessageTypeId() {
            return messageTypeId;
        }

        public void setMessageTypeId(Integer messageTypeId) {
            this.messageTypeId = messageTypeId;
        }

        public Integer getTargetId() {
            return targetId;
        }

        public void setTargetId(Integer targetId) {
            this.targetId = targetId;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }
    }
}
