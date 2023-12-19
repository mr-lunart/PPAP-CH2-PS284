const { v4: uuidv4 } = require('uuid')
const User = require('../models/user.js')
const Pesan = require('../models/pesan.js')
const {Op} = require('sequelize')
const addPesan = async(req,res) =>{
    try {
      const {tanggal, id_rumah_sakit} = req.body  
      if (!tanggal && !id_rumah_sakit) return res.status(400).json({ message: 'tanggal dan id rumah sakit dibutuhkan' })
      const username = req.username
      const searchIdUser = await User.findOne({where: {username}, attributes: ['id_user']})
      const getIdUser = searchIdUser.dataValues.id_user
      const countPesan = await Pesan.count({where: {
        id_user: getIdUser, 
        status: {
            [Op.or]: ['Belum dikonfirmasi', 'Dikonfirmasi', 'Diproses']
        }
      }}) 
      if (countPesan>0) return res.status(400).json({ message: 'pesanan sudah ada' })
      const createPesan = Pesan.create({
    id_pesan:uuidv4(),
    id_user: getIdUser, tanggal, id_rumah_sakit, status: 'Belum dikonfirmasi'
    
    }) 
    if (!createPesan) return res.status(400).json({ message: 'gagal membuat pesanan' })
    return res.status(200).json({ message: 'pesanan berhasil dibuat' })
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}
const getPesan = async(req,res) =>{
    try {
     const username = req.username
     const searchIdUser = await User.findOne({where: {username}, attributes: ['id_user']})
      const getIdUser = searchIdUser.dataValues.id_user
      const searchPesan = await Pesan.findAll({where: {id_user: getIdUser}})
      if (searchPesan.length ===0) return res.status(400).json({ message: 'pesanan tidak tersedia' })
      return res.status(200).json({ message: 'berhasil mengambil pesanan', data: searchPesan }) 

    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}
const putPesan = async(req,res) =>{
    try {
        const {tanggal, id_rumah_sakit, status} = req.body   
        const id_pesan = req.params.id
        if (!id_pesan) return res.status(400).json({ message: 'id pesan dibutuhkan' })
        const username = req.username
        const searchIdUser = await User.findOne({where: {username}, attributes: ['id_user']})
      const getIdUser = searchIdUser.dataValues.id_user
      const searchPesan = await Pesan.findOne({where: {id_user: getIdUser, id_pesan}})
      if (!searchPesan) return res.status(400).json({ message: 'pesanan tidak tersedia' })
        searchPesan.tanggal = tanggal || searchPesan.dataValues.tanggal
        searchPesan.id_rumah_sakit = id_rumah_sakit || searchPesan.dataValues.id_rumah_sakit
        searchPesan.status = status || searchPesan.dataValues.status
        const updatePesan = searchPesan.save()
        if (!updatePesan) return res.status(400).json({ message: 'pesanan gagal diupdate' })
        return res.status(200).json({ message: 'pesanan berhasil diupdate' })


    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}

const getHistory = async(req, res) => {
    try {
        const username = req.username
        const searchIdUser = await User.findOne({where: {username}, attributes: ['id_user']})
      const getIdUser = searchIdUser.dataValues.id_user
      const getHistory = await Pesan.findAll({where: {id_user: getIdUser, status:'Selesai'}})  
      if (getHistory.length ===0)  return res.status(400).json({ message: 'history pemesanan tidak ada'})
      return res.status(200).json({ message: 'berhasil mendapatkan history user', data: getHistory})
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}

module.exports = {getPesan, addPesan, putPesan, getHistory}