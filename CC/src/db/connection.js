const database = require('./database.js')

const connection = async() => {
    try {
        database.authenticate
        console.info('Database terhubung')
    } catch (error) {
        console.error(`Database gagal terhubung, error: ${error.message}`)
    }
}

module.exports = connection