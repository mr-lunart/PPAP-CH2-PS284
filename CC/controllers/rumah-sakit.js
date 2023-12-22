const RumahSakit = require('../models/rumah-sakit.js')
const { v4: uuidv4 } = require('uuid')

const getHospitals = async (req, res) => {
    try {
      const hospitals = await RumahSakit.findAll()
      if (hospitals.length === 0) return res.status(400).json({ message: 'data rumah sakit tidak tersedia' }) 
      return res.status(200).json({ message: 'berhasil mengambil data rumah sakit', data: hospitals }) 
    } catch (error) {
      return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
 }

const getHospital = async (req, res) => { 
    try {
    const id_rumah_sakit = req.params.id
    if (!id_rumah_sakit) return res.status(400).json({ message: 'parameter id dibutuhkan' }) 
    const hospital = await RumahSakit.findOne({where: {id_rumah_sakit}})   
    if (!hospital) return res.status(400).json({ message: 'rumah sakit tidak ditemukan' })
    return res.status(200).json({ message: 'rumah sakit berhasil ditemukan', data: hospital })
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })   
    }
}

const postHospital = async (req, res) => {
    try {
        const { nama_rs, alamat, no_hp, latitude, longitude, type } = req.body

        if (!nama_rs && !alamat && !no_hp && !latitude && !longitude && !type) return res.status(400).json({ message: 'body tidak lengkap' })
        const createdHospital = await RumahSakit.create({
            id_rumah_sakit: uuidv4(), nama: nama_rs, alamat, no_hp, latitude, longitude, type
        })
        if (!createdHospital) return res.status(400).json({ message: 'gagal tambah rumah sakit' })
        return res.status(200).json({ message: 'berhasil tambah rumah sakit' })
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })
    }
}

const putHospital = async (req, res) => { 
    try {
        const id_rumah_sakit = req.params.id
        const { nama_rs, alamat, no_hp, latitude, longitude, type } = req.body
    if (!id_rumah_sakit) return res.status(400).json({ message: 'parameter id dibutuhkan' }) 
    const hospital = await RumahSakit.findOne({where: {id_rumah_sakit}})   
    if (!hospital) return res.status(400).json({ message: 'rumah sakit tidak ditemukan' })
    hospital.nama = nama_rs || hospital.dataValues.nama 
    hospital.alamat = alamat || hospital.dataValues.alamat
    hospital.no_hp = no_hp || hospital.dataValues.no_hp
    hospital.latitude = latitude || hospital.dataValues.latitude 
    hospital.longitude = longitude || hospital.dataValues.longitude 
    hospital.type = type || hospital.dataValues.type
    const updateHospital = await hospital.save()

    if (!updateHospital) return res.status(400).json({ message: 'gagal update rumah sakit' })
    return res.status(200).json({ message: 'berhasil update rumah sakit' })

    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })  
    }
}

const deleteHospital = async (req, res) => {
    try {
        const id_rumah_sakit = req.params.id
    if (!id_rumah_sakit) return res.status(400).json({ message: 'parameter id dibutuhkan' }) 
    const hospital = await RumahSakit.findOne({where: {id_rumah_sakit}})   
    if (!hospital) return res.status(400).json({ message: 'rumah sakit tidak ditemukan' }) 
    const deleteHospital = await RumahSakit.destroy({where: {id_rumah_sakit}})
    if (!deleteHospital.length === 0) return res.status(400).json({ message: 'gagal hapus data rumah sakit' }) 
    return res.status(200).json({ message: 'berhasil hapus data rumah sakit' }) 
    } catch (error) {
        return res.status(500).json({ message: 'internal server error', error: error.message })   
    }
 }

module.exports = {
    getHospitals, getHospital, postHospital, putHospital, deleteHospital
}