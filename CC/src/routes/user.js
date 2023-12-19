const express = require('express')
const userRouter = express.Router()
const userController = require('../controllers/user.js')
const authenticateToken = require('../middleware/authentication.js')

userRouter.post('/auth/login', userController.login)
userRouter.post('/auth/register', userController.register)
userRouter.get('/user/me', authenticateToken, userController.getUsers)
userRouter.put('/user', authenticateToken, userController.putUser)
userRouter.put('/change-password', authenticateToken, userController.changePassword)
module.exports = userRouter