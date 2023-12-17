import numpy as np
import dlib
import tensorflow as tf
import os
import io
from PIL import Image, ImageOps, ImageEnhance
from flask import Flask, jsonify, request
from auth import auth
from joblib import load
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy import text
from google.oauth2 import service_account
from google.cloud import storage
from google.cloud.sql.connector import Connector

app = Flask('verification')
app.config['ALLOWED_EXTENSIONS'] = set(['jpg', 'jpeg'])
app.config['MODEL_FILE'] = 'models/'
project_id = "checkcapstone"
model_bucket_name = "model_face_bucket"
credentials = service_account.Credentials.from_service_account_file("checkcapstone-17084f1a691c.json")
storage_client = storage.Client.from_service_account_json("checkcapstone-17084f1a691c.json")
connector = Connector(credentials=credentials)
model_bucket = storage_client.bucket(model_bucket_name)

predictor = tf.keras.models.load_model(app.config['MODEL_FILE'], compile=False, safe_mode=False)
detector = dlib.get_frontal_face_detector()

def getconn():

    getconn = connector.connect(
        "checkcapstone:us-central1:api-cc",
        "pymysql",
        user="root",
        password="password123456789",
        db="capstone"
    )
    return getconn

engine = create_engine("mysql+pymysql://",creator=getconn,)
Session = sessionmaker(bind=engine)

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']

# def image_to_base64(image):
#     # Convert PIL image to base64 for displaying in HTML
#     buffered = BytesIO()
#     image.save(buffered, format="JPEG")
#     img_str = base64.b64encode(buffered.getvalue()).decode("utf-8")
#     return f"data:image/jpeg;base64,{img_str}"

def convert_and_trim_bb(image, rect):
  startX = 0
  startY = 0
  w = 0
  h = 0
  if len(rect) > 0:
    rect = rect[0]
    # extract the starting and ending (x, y)-coordinates of the bounding box
    startX = rect.left()
    startY = rect.top()
    endX = rect.right()
    endY = rect.bottom()
    # ensure the bounding box coordinates fall within the spatial dimensions of the image
    startX = max(0, startX)
    startY = max(0, startY)
    endX = min(endX, image.shape[1])
    endY = min(endY, image.shape[0])
    # compute the width and height of the bounding box
    w = endX - startX
    h = endY - startY
	  # return our bounding box coordinates

  return (startX, startY, w, h)

def face_detector(image):
  image = Image.open(image)
  image_array = np.array(image)
  faces = detector(image_array)
  crop = convert_and_trim_bb(image_array, faces)

  if sum(crop) > 1 :
    
    cropped_image = image.crop((crop[0],crop[1],crop[0]+crop[2],crop[1]+crop[3]))
    resize_image = cropped_image.resize((160,160))
    enhancer = ImageEnhance.Sharpness(resize_image)
    photo = enhancer.enhance(2)
    img = np.around((np.array(photo) / 255.), decimals=12)
    data_face = np.expand_dims(img, axis=0)

    return data_face
  else:
     return None

def predict_face(image):
  vector = predictor.predict(image)
  return vector

def download_joblib(filename):
  model_file = io.BytesIO()
  blob = model_bucket.blob(filename)
  blob.download_to_file(model_file)
  model = load(model_file)
  return model

@app.route("/")
def index():
    return jsonify({
       "status": {
          "code": 200,
          "message": "Success fetching the API"
       },
       "data": None
    }), 200


@app.route("/verification", methods=["POST"])
@auth.login_required()
def preprocessing():
    if request.method == "POST":
      image = request.files["face_photo"]
      user_id = request.form.get('user_id')
      if image and allowed_file(image.filename):
        face = face_detector(image)
        if type(face) == type(None):
           return jsonify({
              "status": {
                 "code": 200,
                 "message": "request success, face undetected"},
              "data": None
           }),200

        else :
          session = Session()
          user_data = {'user_id':user_id}
          insert_query = text("SELECT user_id,model_path FROM capstone.verification_statue WHERE user_id = :user_id")
          query_result = session.execute(insert_query, user_data).first()
          session.close()
          if_clf = download_joblib(query_result[1])
          vector_face = predict_face(face)
          result = if_clf.predict(vector_face)
          return jsonify({
            "status": {
                "code": 200,
                "message": "request success, face detected"
            },
            "data": f"result,{result}"
          }),200
      
      else:
         return jsonify({
          "status": {
              "code": 422,
              "message": "request success, wrong file extension"
          },
          "data": None
        })
    
    else:
      return jsonify({
        "status": {
            "code": 405,
            "message": "method not allowed"
        },
        "data": None
      })


if __name__ == "verification":
    app.run(debug=False,
            host="0.0.0.0",
            )