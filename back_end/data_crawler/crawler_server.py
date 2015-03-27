from flask import Flask
from flask import request

import crawler

app = Flask(__name__)

@app.route("/fetch/cardetail", methods=["GET", "POST"])
def hello():
    vehicleUniqueId = request.args.get("vehicleUniqueId")
    if not vehicleUniqueId:
    	return "Hello World!"
    ret = crawler.crawlSingleCar(vehicleUniqueId)
    if ret > 0:
    	return "Success!"
    else:
    	return "Failed somehow."

if __name__ == "__main__":
    app.run()
