module lang::rascalcore::compile::Examples::Tst1

//value main(){ //ttest bool keywordParam105(){
//    int f17(int c = 10){
//        int g17(int c = 100){
//            return c;
//        }
//        return g17(c=200);
//    }
//    return f17(c=11);// == 200;
//}

value main(){ //test bool keywordParam107(){
    int f19(int c = 10){
        int h19(){
            int g19(int c = 1, int d = 100){
                return c + d;
            }
            return g19(d=200);
        }
        return h19();
    }
    return f19(c=11);// == 201;
}

//value main(){ //test bool keywordParam105(){
//    int f17(int c = 10){
//        int g17(int c = 100){
//            return c;
//        }
//        return g17(c=200);
//    }
//    return f17(c=11);// == 200;
//}