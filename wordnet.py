
from http.server import BaseHTTPRequestHandler, SimpleHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse
import json
import quopri
from wiki_ru_wordnet import WikiWordnet
from urllib.parse   import unquote

wikiwordnet = WikiWordnet()

class Ontologyresponse :
	def __init__(self, s, h):
		self.synsets = s
		self.hyps =  h

class OntologyresponseDecoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Ontologyresponse):
            return obj.__dict__
        return json.JSONEncoder.default(self, obj)


class HandleRequests(BaseHTTPRequestHandler):
  def _set_headers(self):
    self.send_response(200)
    self.send_header('Content-type', 'application/json')
    self.end_headers()

  def get_synset(self, word):
    result = []
    parents = []
    synsets = wikiwordnet.get_synsets(word)
	
    for s in synsets:
      for w in s.get_words():
        if not (w.lemma() in result):
            result.append(w.lemma())
			
      hyps = wikiwordnet.get_hypernyms(s)
      for h in hyps:
           for hw in h.get_words():
               if not (hw.lemma() in parents) :
                    parents.append(hw.lemma())

    ontResp = Ontologyresponse(result, parents)
	
    json_obj = json.dumps(ontResp, cls=OntologyresponseDecoder)
    return json_obj.encode('utf-8')

  def do_GET(self):
    print("got request for word")
    query = urlparse(self.path).query
    query_components = dict(qc.split("=") for qc in query.split("&"))
    word = unquote(query_components["word"])

    print(word.encode('utf-8'))
    word1 = 'собака'
    print(word1)
	
    self._set_headers()
    self.wfile.write(self.get_synset(word))


handlerClass = HandleRequests
serverClass  = HTTPServer
protocol     = "HTTP/1.0"
port = 8000

server_address = ('127.0.0.1', port)
handlerClass.protocol_version = protocol
httpd = serverClass(server_address, handlerClass)

sa = httpd.socket.getsockname()
httpd.serve_forever() 

	