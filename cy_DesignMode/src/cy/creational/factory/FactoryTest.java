package cy.creational.factory;

public class FactoryTest {
    public static void main(String[] args) {

        FactoryTest test = new FactoryTest();

        Provider provider = test.new SendMailFactory();
        Sender sender = provider.produce();
        sender.Send();
    }

    public interface Sender {
        public void Send();
    }

    public class MailSender implements Sender {
        @Override
        public void Send() {
            System.out.println("this is mailsender!");
        }
    }

    public class SmsSender implements Sender {

        @Override
        public void Send() {
            System.out.println("this is sms sender!");
        }
    }

    public class SendMailFactory implements Provider {

        @Override
        public Sender produce() {
            return new MailSender();
        }
    }

    public class SendSmsFactory implements Provider {

        @Override
        public Sender produce() {
            return new SmsSender();
        }
    }

    public interface Provider {
        public Sender produce();
    }

}
