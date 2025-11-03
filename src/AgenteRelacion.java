package src;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgenteRelacion extends Agent {

    protected void setup() {
        System.out.println("Agente "+getLocalName()+" ha empezado");
        // ACLMessage msg = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		// System.out.println("Agent "+getLocalName()+": REQUEST message received. Reply and exit.");
		
		// doDelete();
    } 
}
