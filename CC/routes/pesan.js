const express = require('express')
const pesanRouter = express.Router()
const pesanController = require('../controllers/pesan.js')
const authenticateToken = require('../middleware/authentication.js')


pesanRouter.post('/pesan', authenticateToken, pesanController.addPesan)
pesanRouter.get('/pesan/me', authenticateToken, pesanController.getPesan)
pesanRouter.get('/history', authenticateToken, pesanController.getHistory)
pesanRouter.put('/pesan/:id', authenticateToken, pesanController.putPesan)

module.exports = pesanRouter