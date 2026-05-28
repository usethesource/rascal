module util::Webclient

extend Content;
import Exception;

@javaClass{org.rascalmpl.library.util.Webclient}
java Response fetch(Request request);

@synopsis{Short-hand for construction of JSON post bodies}
Response postJSON(loc url, value content, JSONOptions options=jsonOptions()) 
    = fetch(post(url.path, send(json(options=options), content, mimeType="application/json", charset=DEFAULT_CHARSET), host=url[path=""]));

@synopsis{Short-hand for construction of JSON post bodies}
Response putJSON(loc url, value content, JSONOptions options=jsonOptions()) 
    = fetch(put(url.path, send(json(options=options), content,  mimeType="application/json", charset=DEFAULT_CHARSET), host=url[path=""]));

@synopsis{Short-hand for construction of JSON post bodies}
Response putFile(loc url, loc content) 
    = fetch(put(url.path, send(file(), content), host=url[path=""]));

@synopsis{Exemple use of the fetch API to download a HTML page and parse it immediately}
HTMLElement fetchWebPage(loc url)
    = fetch(get(url.path, host=url[path=""])).body.receiver(html(), #HTMLElement);

@synopsis{Exemple use of the fetch API to call a JSON parametrized webservice and read the response as JSON}
&T callWebService(loc url, value parameters, type[&T] expected)
    = postJSON(url, parameters).body.receiver(json(), expected);
