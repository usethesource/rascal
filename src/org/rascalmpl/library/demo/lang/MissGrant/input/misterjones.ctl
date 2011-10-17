events
 fridgeOpened F1OP
 candleStickTurned CSTR
 candleStickTurnedBack CSTB
 fireplaceLit FPLT
 fireOut FPOT
 bookTaken BKTK
 bookPlaced BKPL
 gateOpened GTOP
 gateClosed GTCL 
end 

resetEvents
 gateOpened
 candleStickTurnedBack
 bookPlaced
 fireOut
end

commands
 activateTurrets ACTU
 lockGate GTLK
 unlockGate GTUK
 turnBookCase TRBC
 turnBackBookCase TBBC
 turnBackCandleStick TBCS
end 	

state idle	
 actions {unlockGate turnBackBookCase turnBackCandleStick}
 gateClosed => waitingForRest
end 

state waitingForRest
 fridgeOpened => waitCandleFireBook 
 candleStickTurned => waitFridgeFireBook
 fireplaceLit => waitFridgeCandleBook
end

state waitCandleFireBook
 candleStickTurned => waitFireBook
 fireplaceLit => waitCandleBook
 bookTaken => waitCandleFire
end 

state waitFridgeFireBook
 fridgeOpened => waitFireBook 
 fireplaceLit => waitFridgeBook
 bookTaken => waitFridgeFire
end 

state waitFridgeCandleBook
 candleStickTurned => waitFridgeBook
 bookTaken => waitFridgeCandle
 fridgeOpened => waitCandleBook 
end 

state waitFireBook
 fireplaceLit => waitBook 
 bookTaken => waitFire
end 

state waitCandleBook
 candleStickTurned => waitBook 
 bookTaken => waitCandle
end 

state waitFridgeBook
 fridgeOpened => waitBook 
 bookTaken => waitFridge
end 


state waitFridgeFire
 fireplaceLit => waitFridge 
 fridgeOpened => waitFire
end 

state waitCandleFire
 candleStickTurned => waitFire 
 fireplaceLit => waitCandle 
end 

state waitFridgeCandle
 fridgeOpened => waitCandle
 candleStickTurned => waitFridge
end

state waitBook
 actions {lockGate activateTurrets}
 bookTaken => active
end

state waitFire
 actions {lockGate activateTurrets}
 fireplaceLit => active
end

state waitCandle
 actions {lockGate activateTurrets}
 candleStickTurned => active
end

state waitFridge
 actions {lockGate activateTurrets}
 fridgeOpened => active
end

state active
 actions {turnBookCase}
end