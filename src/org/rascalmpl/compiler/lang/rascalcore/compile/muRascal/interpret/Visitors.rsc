module lang::rascalcore::compile::muRascal::interpret::Visitors

import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;

extend lang::rascalcore::compile::muRascal::interpret::Eval;

import util::Reflective;
import Node;

alias TResult = tuple[value result, bool matched, bool changed]; 

Result traverse(value subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor, Env env){
    bool isBottomUp = vdescriptor.direction;
    bool isTopDown = !isBottomUp;
    
    bool isContinuing = vdescriptor.progress;
    bool isBreaking = !isContinuing;
    
    bool isFixedPoint = vdescriptor.fixedpoint;
    bool rebuild = vdescriptor.rebuild;
    
    TResult traverseTop(value subject){
        fp = getFingerprint(subject, vdescriptor.descendant.useConcreteFingerprint);
     
        for(muCase(int fingerprint, MuExp exp) <- cases){
            if (debug) println("fp = <fp>; case <fingerprint>, <exp>");
            if(fingerprint == fp){
                try {
                    <v, env> = eval(exp, env);
                    return <subject, true, false>;
                } catch doInsert(rvalue(subject1), env1):{
                    return <subject1, true, true>;
                }
            }
        }
        try {
            <v, env1> = eval(defaultExp, env);
            return <subject, true, false>;
            
        } catch doInsert(rvalue(subject1), env1):{
            return <subject1, true, true>;
        }
    
    }
    
    TResult traverseOnce(value subject){
        value result = subject;

        bool hasMatched = false;
        bool hasChanged = false;
        
        if (isTopDown){
            <newTop, hasMatched, hasChanged> = traverseTop(subject);

            if (isBreaking && hasMatched()) {
                return <newTop, hasMatched, hasChanged>;
            }
            else if (isFixedPoint && hasChanged) {
                do {
                    <newTop, hasMatched, hasChanged> = traverseTop(newTop);
                } while (hasChanged);
                hasChanged = true;
                subject = newTop;
            }
            else {
                subject = newTop;
            }
        }
        
        //if(isConcreteMatch){
        //    if(tr.shouldDescentInConcreteValue((ITree)subject)){
        //        result = traverseConcreteTreeOnce(subject, tr);
        //    }
        //} else {
            //if(tr.shouldDescentInAbstractValue(subject)){
            
                <result, hasMatched, hasChanged> = traverseTypeOnce(subject);
                
         //   }
        //}
        
        //if (isTopDown) {
        //    tr.setMatchedAndChanged(tr.hasMatched() | hasMatched,
        //                            tr.hasChanged() | hasChanged);
        //}

        if (isBottomUp) {
            if ((isBreaking && hasMatched)) {
                return <result, hasMatched, hasChanged>;
            }

            hasMatched1 = hasMatched;
            hasChanged1 = hasChanged;
            
            <result, hasMatched, hasChanged> = traverseTop(result);
            
            if (hasChanged && isFixedPoint) {
                do {
                    <result, hasMatched, hasChanged> = traverseTop(result);
                } while (hasChanged);
            }
            hasMatched = hasMatched1 || hasMatched;
            hasChanged = hasChanged1 || hasChanged;
        }
        
        return <result, hasMatched, hasChanged>;
    }
    
    TResult traverseTypeOnce(map[value,value] subject){
        if(isEmpty(subject))
            return <subject, false, false>;
            
        bool hasMatched = false;
        bool hasChanged = false;
        result = ();
        for(key <- subject){
            <key1, hasMatched1, hasChanged1> = traverseOnce(key);
            hasMatched = hasMatched || hasMatched1;
            hasChanged = hasChanged || hasChanged1;
            
            <val1, hasMatched1, hasChanged1> = traverseOnce(subject[key]);
            hasMatched = hasMatched || hasMatched1;
            hasChanged = hasChanged || hasChanged1;
            
            result[key1] = val1;
        }
      
        return < hasChanged ? result : subject, hasMatched, hasChanged >;
    }
    
    TResult traverseTypeOnce(set[value] subject){
        if(isEmpty(subject))
            return <subject, false, false>;
            
        bool hasMatched = false;
        bool hasChanged = false;
        result = {};
        for(v <- subject){
            <v1, hasMatched1, hasChanged1> = traverseOnce(v);
            hasMatched = hasMatched || hasMatched1;
            hasChanged = hasChanged || hasChanged1;
            result += v1;
        }
       
        return <hasChanged ? result : subject, hasMatched, hasChanged>;
    }
    
    TResult traverseTypeOnce(list[value] subject){
        if(isEmpty(subject))
            return <subject, false, false>;
            
        bool hasMatched = false;
        bool hasChanged = false;
        result = [];
        for(v <- subject){
            <v1, hasMatched1, hasChanged1> = traverseOnce(v);
            hasMatched = hasMatched || hasMatched1;
            hasChanged = hasChanged || hasChanged1;
            result += v1;
        }
        if(hasChanged){
            return <result, hasMatched, hasChanged>;
        } else {
            return <subject, hasMatched, hasChanged>;
        }
    }
    
    TResult traverseTypeOnce(tuple[value t1] subject){
        bool hasMatched = false;
        bool hasChanged = false;
       
        <v1, hasMatched1, hasChanged1> = traverseOnce(subject.t1);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
      
        return < hasChanged ? <v1> : subject, hasMatched, hasChanged >;
    }
    
    TResult traverseTypeOnce(tuple[value t1, value t2] subject){
        bool hasMatched = false;
        bool hasChanged = false;
       
        <v1, hasMatched1, hasChanged1> = traverseOnce(subject.t1);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
        
        <v2, hasMatched1, hasChanged1> = traverseOnce(subject.t2);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
      
        return < hasChanged ? <v1, v2> : subject, hasMatched, hasChanged>;
    }
    
     TResult traverseTypeOnce(tuple[value t1, value t2, value t3] subject){
        bool hasMatched = false;
        bool hasChanged = false;
       
        <v1, hasMatched1, hasChanged1> = traverseOnce(subject.t1);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
        
        <v2, hasMatched1, hasChanged1> = traverseOnce(subject.t2);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
        
        <v3, hasMatched1, hasChanged1> = traverseOnce(subject.t3);
        hasMatched = hasMatched || hasMatched1;
        hasChanged = hasChanged || hasChanged1;
      
        return < hasChanged ? <v1, v2, v3> : subject, hasMatched, hasChanged >;
    }
    
    // TODO add more tuple cases
    
    TResult traverseTypeOnce(node subject){
        if(constructor(AType ctype, list[value] fields, map[str,value] kwparams) := subject){
            <args, hasMatchedArgs, hasChangedArgs> = traverseOnce(fields);
            <vkwParams, hasMatchedKwParams, hasChangedKwParams> = traverseOnce(kwparams);
        
            hasMatched = hasMatchedArgs || hasMatchedKwParams;
            hasChanged = hasChangedArgs || hasChangedKwParams;
            if(map[str,value] kwParams := vkwParams){
                return < hasChanged ? makeConstructor(ctype, args, kwParams) : subject, hasMatched, hasChanged >;
            }
            throw "traverseTypeOnce, constructor, kwParams: <vkwParams>";
        }
        <args, hasMatchedArgs, hasChangedArgs> = traverseOnce(getChildren(subject));
        <vkwParams, hasMatchedKwParams, hasChangedKwParams> = traverseOnce(getKeywordParameters(subject));
        
        hasMatched = hasMatchedArgs || hasMatchedKwParams;
        hasChanged = hasChangedArgs || hasChangedKwParams;
        if(map[str,value] kwParams := vkwParams){
            return < hasChanged ? makeNode(getName(subject), args, keywordParameters=kwParams) : subject, hasMatched, hasChanged >;
        }
        throw "traverseTypeOnce, node, kwParams: <vkwParams>";
    }    
    
    default TResult traverseTypeOnce(value subject)
        = <subject, false, false>;
    
    // Body of traverse    
        
    <result, hasMatched, hasChanged> = traverseOnce(subject);
    return <rvalue(result), env>;
}