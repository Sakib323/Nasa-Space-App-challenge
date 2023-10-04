import numpy as np
from flask import Flask, request, jsonify, render_template, url_for
from keras.models import load_model
app = Flask(__name__)
model = load_model("lightening_classfier.h5")
@app.route('/')
def home():
    return jsonify("this is lightening classifier")
@app.route('/predict_api', methods=['POST'])
def predict_api():
    data = request.json
    T2M = data.get('T2M')
    T2MDEW = data.get('T2MDEW')
    T2MWET = data.get('T2MWET')
    QV2M = data.get('QV2M')
    RH2M = data.get('RH2M')
    PRECTOTCORR = data.get('PRECTOTCORR')
    PS = data.get('PS')
    WS10M = data.get('WS10M')
    WD10M = data.get('WD10M')
    WS50M = data.get('WS50M')
    WD50M = data.get('WD50M')
    x = np.asarray([T2M, T2MDEW, T2MWET, QV2M, RH2M, PRECTOTCORR, PS, WS10M, WD10M, WS50M, WD50M])
    prediction = model.predict(x.reshape(1, -1))
    prediction_list = prediction.tolist()
    return jsonify({'prediction': prediction_list})
if __name__ == '__main__':
    app.run(debug=True)
