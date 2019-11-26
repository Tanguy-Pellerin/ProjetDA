package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

import demo.Message.*;


public class MyActor extends UntypedAbstractActor {

	private int value;
	private int seq;
	private int rseq;
	private ArrayList<ActorRef> actorList;

	private int nbrResponse;


	private WriteMessage writeMessage;
	private MessageACK messageACK;
	private MessageReceive messageReceive;
	private ListActor messageList;
	private WriteOrder writeOrder;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public MyActor() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(MyActor.class, () -> {
			return new MyActor();
		});
	}

	static public class MyMessage {
		public final String data;

		public MyMessage(String data) {
			this.data = data;
		}
	}


	public void onReceive(Object message) throws Throwable {
		if(message instanceof WriteOrder) {
			writeOrder = (WriteOrder)message;
			this.value = writeOrder.v;

			this.seq++;
			WriteMessage writeMessage = new WriteMessage(this.seq, this.value);
			log.info("["+getSelf().path().name()+"] broadcast the data ["+value+"]");
			for (ActorRef actor : actorList) {
				actor.tell(writeMessage, getSelf());
			}
			while (nbrResponse > (actorList.size())/2) {

			}
		}

		if(message instanceof WriteMessage) {
			writeMessage = (WriteMessage)message;
			if(writeMessage.seq > this.seq) {
				this.seq = writeMessage.seq;
				this.value = writeMessage.v;
				log.info("["+getSelf().path().name()+"] received the value ["+this.value+"] from ["+getSender().path().name()+"]");

			}
			getSender().tell(new MessageReceive(seq), getSelf());
		}

		if(message instanceof MessageReceive) {
			messageReceive = (MessageReceive) message;
			log.info("["+getSender().path().name()+"] respond to ["+getSelf().path().name()+"]");
			if(this.seq <= messageReceive.seq) {
				nbrResponse++;
			}
		}

		if (message instanceof ListActor) {
			messageList = (ListActor)message;
			this.actorList = messageList.list;
			log.info("["+getSelf().path().name()+"] received a list from ["+getSender().path().name()+"]");
		}

		if (message instanceof GetValue) {
			log.info("["+getSelf().path().name()+"] have the value ["+this.value+"]");
		}
	}
/*
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(MyMessage.class, this::receiveFunction)
			.build();
	  }

	  public void receiveFunction(MyMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
	  }
*/

}
