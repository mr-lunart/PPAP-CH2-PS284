const User = require('../models/user.js')
const bcrypt = require('bcrypt')
const { v4: uuidv4 } = require('uuid')
const jwt = require('jsonwebtoken')


const register = async (req, res) => {

    try {
        const { username, password, no_hp, nama } = req.body
        const searchUserInDatabase = await User.count({ where: { username } })
        if (searchUserInDatabase > 0) return res.status(400).json({ message: 'username sudah digunakan' })
        const searchUserInDatabaseByNo_hp = await User.count({ where: { no_hp } })
        if (searchUserInDatabaseByNo_hp > 0) return res.status(400).json({ message: 'nomor hp sudah digunakan' })
        const hashPassword = await bcrypt.hash(password, 10)
        const createUser = User.create({
            id_user: uuidv4(),
            nama, username, no_hp, password: hashPassword
        })

        if (!createUser) return res.status(400).json({ message: 'register gagal' })
        return res.status(200).json({ message: 'register berhasil' })

    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })
    }
}
const login = async (req, res) => {
    try {
        const { username, password } = req.body
        const searchUserInDatabase = await User.findOne({ where: { username } })
        if (!searchUserInDatabase) return res.status(400).json({ message: 'username dan password salah' })
        const userPassword = searchUserInDatabase.dataValues.password
        const matchPassword = await bcrypt.compare(password, userPassword)
        if (!matchPassword) return res.status(400).json({ message: 'username dan password salah' })
        const SECRET_KEY = process.env.SECRET_KEY
        const token = jwt.sign({ username }, SECRET_KEY, { expiresIn: '1d' })
        return res.status(200).json({ message: 'login berhasil', data: { token } })
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })
    }
}
const getUsers = async (req, res) => {
    try {
        const username = req.username
        const user = await User.findOne({where: {username}})
        if (!user) return res.status(400).json({ message: 'user tidak tersedia' })
        return res.status(200).json({ message: 'berhasil mengambil data saya', data: {
            id_user: user.dataValues.id_user, 
            nama: user.dataValues.nama, 
            username: user.dataValues.username,
            no_hp: user.dataValues.no_hp, 
             
        }})
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message }) 
    }
 }

const putUser = async(req,res) => {
    try {
        const username = req.username
        const {nama, no_hp} = req.body
        const user = await User.findOne({where: {username}}) 
        if (!user) return res.status(400).json({ message: 'user tidak tersedia' })
        user.nama = nama || user.dataValues.nama
        user.no_hp = no_hp || user.dataValues.no_hp

        const updateUser = await user.save()
        if (!updateUser) return res.status(400).json({ message: 'update user gagal' })
        return res.status(200).json({ message: 'update user berhasil' })

    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })
    }  
    
}

const changePassword = async(req, res) => {
    try {
      const {oldPassword, newPassword, confirmNewPassword} = req.body
      if (!oldPassword && !newPassword && !confirmNewPassword) return res.status(400).json({ message: 'body harus diisi' })
        const equalPassword = newPassword === confirmNewPassword? true: false
       if (!equalPassword) return res.status(400).json({ message: 'new password dan confirm new password tidak sesuai' }) 
       const username = req.username 
       const user = await User.findOne({where: {username}}) 
       if (!user) return res.status(400).json({ message: 'user tidak tersedia' })
        const matchPassword = await bcrypt.compare(oldPassword, user.dataValues.password)
        if (!matchPassword) return res.status(400).json({ message: 'password salah' })
        user.password = await bcrypt.hash(newPassword, 10)
        const updateUser = await user.save()
        if (!updateUser) return res.status(400).json({ message: 'update password gagal' })
        return res.status(200).json({ message: 'update password berhasil' })

    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}

module.exports = {
    register, login, getUsers, putUser, changePassword
}