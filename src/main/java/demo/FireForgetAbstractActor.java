package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

import demo.MyActor.MyMessage;
import demo.Message.*;

/**
 * @author Remi SHARROCK
 * @description
 */
public class FireForgetAbstractActor {

	public static void main(String[] args) {

		ArrayList<ActorRef> actorList = new ArrayList<>();


		final ActorSystem system = ActorSystem.create("system");


		actorList.add(system.actorOf(MyActor.createActor(), "a"));
		actorList.add(system.actorOf(MyActor.createActor(), "b"));
		
		// Instantiate first and second actor
	    //final ActorRef a = system.actorOf(MyActor.createActor(), "a");
	    
			// send to a1 the reference of a2 by message
			//be carefull, here it is the main() function that sends a message to a1, 
			//not a1 telling to a2 as you might think when looking at this line:
			MyMessage m = new MyMessage("hello");
			ListActor list = new ListActor(actorList);
			WriteOrder writeOrder = new WriteOrder(1);

	    
	    //a.tell(m, ActorRef.noSender());
	    actorList.get(0).tell(m, ActorRef.noSender());

	    for (ActorRef actor: actorList) {
	    	actor.tell(list, ActorRef.noSender());
	    }
	    actorList.get(0).tell(writeOrder, ActorRef.noSender());
	    actorList.get(1).tell(new WriteOrder(4), ActorRef.noSender());
	    actorList.get(0).tell(new WriteOrder(2), ActorRef.noSender());

	
	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
