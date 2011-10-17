events
 drapesClosed DPCL
 drapesOpened DPOP
 doorClosed D1CL
 brickTouched BRTC
 drawerOpened D2OP
 lightOn L1ON
 doorOpened D1OP
 safeClosed SFCL 
end 

resetEvents
 doorOpened drapesOpened 
end

commands
 unlockSafe SFUN
 lockSafe LKSF
 lockDoor D1LK
 unlockDoor D1UL
end 	

state idle	
 actions {unlockDoor lockSafe}
 doorClosed => active 
end 

state active
 actions {lockDoor}
 drapesClosed => waitingForLight
end 

state waitingForLight
 lightOn => waitingForBrick 
end 

state waitingForBrick
 brickTouched => waitingForDrawer 
end 

state waitingForDrawer
 drawerOpened => unlockedSafe 
end 


state unlockedSafe
 actions {unlockSafe}
 safeClosed => idle 
end