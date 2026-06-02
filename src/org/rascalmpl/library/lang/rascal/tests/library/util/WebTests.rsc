module lang::rascal::tests::library::util::WebTests

import util::Webserver;
import util::Webclient;
import lang::html::AST;

int port = 10003;

private bool testRoundtrip(bool (loc host) testClient) {    
    shutdown(port); // in case one is left from before

    try {
        // start the test server that does not lock the interpreter
        startEchoServerJava(port=port);
        return testClient(|http://localhost:<"<port>">|);
    }
    catch value e: {
        throw e;
    }
    finally {
        // shutdown(port);
        ;
    }
}

test bool testGet() 
    = testRoundtrip(bool (loc host) {
        return fetch(get("/get", host=host)).body.receiver(text(), #str) 
            == "ok";
    });

test bool testPostJSON()
    = testRoundtrip(bool (loc host) {
        node example = "main"(name="Yogi Bear");
        return fetch(post("/post/json", send(json(), example), host=host))
                .body
                .receiver(json(), #node) 
            == example;
    });

test bool testPutJSON()
    = testRoundtrip(bool (loc host) {
        node example = "main"(name="Yogi Bear");
        return fetch(put("/put/json", send(json(), example), host=host))
                .body
                .receiver(json(), #node) 
            == example;
    });

test bool testPostHTML()
    = testRoundtrip(bool (loc host) {
        HTMLElement example = ul([li([text("one")]), li([text("two")])]);
        return fetch(post("/post/html", send(json(), example), host=host))
                .body
                .receiver(html(), #HTMLElement) 
            == example;
    });