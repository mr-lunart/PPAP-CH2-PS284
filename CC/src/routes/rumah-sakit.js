const express = require('express')
const rumahSakitRouter = express.Router()
const rumahSakitController = require('../controllers/rumah-sakit.js')
const authenticateToken = require('../middleware/authentication.js')

rumahSakitRouter.get('/hospitals', authenticateToken, rumahSakitController.getHospitals)
rumahSakitRouter.get('/hospital/:id', authenticateToken, rumahSakitController.getHospital)
rumahSakitRouter.post('/hospital', authenticateToken, rumahSakitController.postHospital)
rumahSakitRouter.put('/hospital/:id', authenticateToken, rumahSakitController.putHospital)
rumahSakitRouter.delete('/hospital/:id', authenticateToken, rumahSakitController.deleteHospital)
module.exports = rumahSakitRouter