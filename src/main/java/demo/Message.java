package demo;

import java.io.Serializable;
import java.util.ArrayList;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;


public class Message {

	static public class WriteOrder {
		public final int v;

		public WriteOrder(int v) {
			this.v = v;
		}
	}

	static public class WriteMessage {
		public final int seq;
		public final int v;

		public WriteMessage(int seq, int v) {
			this.seq = seq;
			this.v = v;
		}
	}


	static public class MessageACK {
		public final int seq;
		public final boolean ACK;

		public MessageACK(int seq, boolean ACK) {
			this.seq = seq;
			this.ACK = ACK;
		}
	}

	static public class MessageReceive {
		public final int seq;

		public MessageReceive(int seq) {
			this.seq = seq;
		}
	}

	static public class GetValue {

		public GetValue() {

		}
	}

	static public class ListActor implements Serializable {
		private static final long serialVersionUID = 1L;
		public final ArrayList<ActorRef> list;

		public ListActor(ArrayList<ActorRef> list) {
			this.list = list;
			//this.list = org.apache.commons.lang3.SerializationUtils.clone(list);
		}
	}
}
