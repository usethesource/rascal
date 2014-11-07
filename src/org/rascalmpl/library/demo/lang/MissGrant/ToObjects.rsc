module demo::lang::MissGrant::ToObjects

import demo::lang::MissGrant::AST;

public str instantiateController(Controller ctl) =
         "<for (e <- ctl.events) {>
         'Event <e.name> = new Event(\"<e.name>\", \"<e.\token>\");
         '<}>
         '<for (c <- ctl.commands) {>
         'Event <c.name> = new Command(\"<c.name>\", \"<c.\token>\");
         '<}>
         '<for (s <- ctl.states) {>
         'State <s.name> = new State(\"<s.name>\");
         '<}>
         'StateMachine machine = new StateMachine(<initial(ctl).name>);
         '<for (s <- ctl.states, t <- s.transitions) {>
         '<s.name>.addTransition(<t.event>, <t.state>);
         '<}>
         '<for (s <- ctl.states, a <- s.actions) {>
         '<s.name>.addAction(<a>);
         '<}>
         '<for (r <- ctl.resets) {>
         'machine.addResetEvents(<r>);
         '<}>
         ";




