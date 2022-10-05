/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.library.lang.rascal.tutor.questions;

import java.util.Random;

public class Feedback {
  
  static Random random = new Random();
  
  private static String[] positiveFeedback = {
  "Good!",
  "Go on like this!",
  "I knew you could make this one!",
  "You are making good progress!",
  "Well done!",
  "Yes!",
  "More kudos",
  "Correct!",
  "You are becoming a pro!",
  "You are becoming an expert!",
  "You are becoming a specialist!",
  "Excellent!",
  "Better and better!",
  "Another one down!",
  "You are earning a place in the top ten!",
  "Learning is fun, right?",
  "Each drop of rain makes a hole in the stone.",
  "A first step of a great journey.",
  "It is the journey that counts.",
  "The whole moon and the entire sky are reflected in one dewdrop on the grass.",
  "There is no beginning to practice nor end to enlightenment; There is no beginning to enlightenment nor end to practice.",
  "A journey of a thousand miles begins with a single step.",
  "When you get to the top of the mountain, keep climbing.",
  "No snowflake ever falls in the wrong place.",
  "Sitting quietly, doing nothing, spring comes, and the grass grows by itself.",
  "To follow the path, look to the master, follow the master, walk with the master, see through the master, become the master.",
  "When you try to stay on the surface of the water, you sink; but when you try to sink, you float.",
  "If you realize that you have enough, you are truly rich.",
  "Experience this moment to its fullest.",
  "Many paths lead from the foot of the mountain, but at the peak we all gaze at the full moon.",
  "As a solid rock is not shaken by the wind, even so the wise are not ruffled by praise or blame.",
  "When you get there, there isn't any there there.",
  "At the still-point in the center of the circle one can see the infinite in all things.",
  "The whole moon and the entire sky are reflected in one dewdrop on the grass.",
  "When the question is sand in a bowl of boiled rice, the answer is a stick in the soft mud."
  };

  private static String[] negativeFeedback = {
  "A pity!",
  "A shame!",
  "Try another question!",
  "I know you can do better.",
  "Keep trying.",
  "I am suffering with you :-(",
  "Give it another try!",
  "With some more practice you will do better!",
  "Other people mastered this, and you can do even better!",
  "It is the journey that counts!",
  "Learning is fun, right?",
  "After climbing the hill, the view will be excellent.",
  "Hard work will be rewarded!",
  "There\'s no meaning to a flower unless it blooms.",
  "Not the wind, not the flag; mind is moving.",
  "If you understand, things are just as they are; if you do not understand, things are just as they are.",
  "Knock on the sky and listen to the sound.",
  "The ten thousand questions are one question. If you cut through the one question, then the ten thousand questions disappear.",
  "To do a certain kind of thing, you have to be a certain kind of person.",
  "When the pupil is ready to learn, a teacher will appear.",
  "If the problem has a solution, worrying is pointless, in the end the problem will be solved. If the problem has no solution, there is no reason to worry, because it can\'t be solved.",
  "And the end of all our exploring will be to arrive where we started and know the place for the first time.",
  "It is better to practice a little than talk a lot.",
  "Water which is too pure has no fish.",
  "All of the significant battles are waged within the self.",
  "No snowflake ever falls in the wrong place.",
  "It takes a wise man to learn from his mistakes, but an even wiser man to learn from others.",
  "Only when you can be extremely pliable and soft can you be extremely hard and strong.",
  "Sitting quietly, doing nothing, spring comes, and the grass grows by itself.",
  "The obstacle is the path.",
  "To know and not do is not yet to know.",
  "The tighter you squeeze, the less you have.",
  "When you try to stay on the surface of the water, you sink; but when you try to sink, you float.",
  "Where there is will, there is a way.",
  "The grass is always greener on the other side.",
  "Change how you see and see how you change.",
  "If you understand, things are just as they are. If you do not understand, things are just as they are.",
  "The wild geese do not intend to cast their reflections.The water has no mind to receive their images.",
  "No matter how hard the past, you can always begin again.",
  "You can't stop the waves, but you can learn how to surf.",
  "Scratch first. Itch later ... ",
  "It takes a wise man to learn from his mistakes, but an even wiser man to learn from others.",
  "Nature does not hurry, yet everything is accomplished.",
  "One gains by losing and loses by gaining.",
  "Relearn everything.  Let every moment be a new beginning.",
  "The world is won by those who let it go.",
  "Normally, we do not so much look at things as we overlook them.",
  "If you want a certain thing, first be a certain person. Then obtaining that certain thing will no longer be a concern.",
  "Nothing ever goes away, until it has taught us what we need to know.",
  "Don't get stuck anywhere, keep flowing like a river.",
  "The birds always find their way to their nests. The river always finds its way to the ocean.",
  "Do not seek the truth, only cease to cherish your opinions.",
  "The resistance to the unpleasant situation is the root of suffering.",
  "For things to reveal themselves to us, we need to be ready to abandon our views about them.",
  "To deny the reality of things is to miss their reality",
  "View all problems as challenges."
  };

  private static String quote(String s){
    return "\"" + s + "\"";
  }
  public static String give(boolean ok) {
    if(random.nextInt(10) == 5){
      if(ok){
        return quote(positiveFeedback[random.nextInt(positiveFeedback.length)]);
      } else {
        return quote(negativeFeedback[random.nextInt(negativeFeedback.length)]);
      }
    }
    return quote("");
  }
}
