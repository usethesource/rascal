@synopsis{Documents a part of the ANSI standard for terminal control sequences}
module lang::std::ANSI

lexical ControlCode 
    = bell          : [\a07]
    | backspace     : [\a07]
    | tab           : [\a09]
    | lineFeed      : [\a0A]
    | formFeed      : [\a0C]
    | carriageReturn: [\a0D]
    | escape        : [\a1B]
    ;

lexical ControlSequenceIntroducer = [\a1B] [\[];

lexical SpecialControlSequence = controlSequence: ControlSequenceIntroducer csi CSISequence code;

lexical CSISequence 
    = cursorUp                   :  Number n [A]
	| cursorDown                 :  Number n [B]
	| cursorForward              :  Number n [C]
	| cursorBack                 :  Number n [D]
	| cursorNextLine             :  Number n [E]
	| cursorPreviousLine         :  Number n [F]
	| cursorHorizontalAbsolute   :  Number n [G]
	| cursorPosition             :  Number n [;] Number m [H]
	| clearToEndOfScreen         :  Number n [0]? [J]
	| clearToBeginningOfScreen   :  Number n [1] [J]
	| clearEntireScreen          :  Number n [2] [J]
	| clearEntireScreenAndBuffer :  Number n [3] [J]
	| clearToEndOfLine           :  "0" [K]
	| clearToBeginningOfLine     :  "1" [K]
	| clearEntireLine            :  "2" [K]
	| scrollUp                   :  Number n [S] 
	| scrollDown                 :  Number n [T] 
	| horizontalVerticalPosition :  Number n [;] Number m [f]
	| selectGraphicsRendition    :  Rendition r [m]
    ;

lexical Rendition
    = reset                         : "0"
	| bold                          : "1"
	| faint                         : "2"	
	| italic                        : "3"
	| underline                     : "4"
	| slowBlink                     : "5"
	| rapidBlink                    : "6"
	| reverse                       : "7"
	| conceal                       : "8"
	| crossedOut                    : "9"
	| primaryFont                   : "10"
	| alternativeFont               : "1" FontChoice
	| fraktur                       : "20"
	| doubleUnderline               : "21"
	| normalIntensity               : "22"
	| neitherItalicNorBlackletter   : "23"
	| notUnderlined                 : "24"
	| notBlinking                   : "25"	
	| proportionalSpacing           : "26"
	| notReversed                   : "27"
	| notConcealed                  : "28"
	| notCrossedOut                 : "29"
	| setForegroundColor            : "3" ColorChoice
	| setForegroundColorRGB         : "38" "5" [;] Number color 
    | setForegroundColorRGB         : "38" "2" [;] Number red [;] Number green [;] Number blue
	| setDefaultForegroundColor     : "39"
    | setBackgroundColor            : "4" ColorChoice
    | setBackgroundColorRGB         : "48" "5" [;] Number color 
    | setBackgroundColorRGB         : "48" "2" [;] Number red [;] Number green [;] Number blue
	| setDefaultBackgroundColor     : "49"
	| disableProportionalSpacing    : "50"
	| framed                        : "51"
	| encircled                     : "52"
	| overlined                     : "53"
	| neitherFramedNorEncircled     : "54"
	| notOverlined                  : "55"
	| setUnderlineColor             : "58" "5" [;] Number color
    | setUnderlineColor             : "58" "2" [;] Number red [;] Number green [;] Number blue
	| defaultUnderlineColor         : "59"
	| rightsideLine                 : "60"	
	| rightsideDoubleLine           : "61"	
	| leftsideLine                  : "62"
	| leftsideDoubleLine            : "63"
	| stressMarking                 : "64"
	| resetAllLines                 : "65"
	| superscript                   : "73"	
	| subscript                     : "74"	
	| neitherSubscriptNorSuperScript: "75"
	| setBrightForegroundColor      : "9" ColorChoice
	| setBrightBackgroundColor      : "10" ColorChoice
    ;

lexical Number      = [0-9]+;
lexical ColorChoice = [0-7];
lexical FontChoice  = [0-9];

@synopsis{Convenience function to construct a list of digits}
Number number(int n) = [Number] "<n>";

ColorChoice colorChoice(int n) = [ColorChoice] "<n>" when 0 <= n, n <= 7;

FontChoice fontChoice(int n) = [FontChoice] "<n>" when 0 <= n, n <= 9;