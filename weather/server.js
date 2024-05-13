const express = require('express');
const app = express();
const csv = require('csv-writer').createObjectCsvWriter;
const moment = require('moment-timezone');
const PORT = 8080;


// Middleware para analisar o corpo das solicitações JSON
app.use(express.json());

// Middleware para analisar o corpo das solicitações de dados de formulário
app.use(express.urlencoded({ extended: true }));

// Middleware para manipulação de erros
app.use((err, req, res, next) => {
    console.error('An error occurred:', err);
    res.status(500).send('An error occurred');
});

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});


app.post('/update-csv', (req, res) => {
    console.log(req.body);

    const csvWriter = csv({
        path: 'weather.csv',
        header: [
            {id: 'temperature', title: 'TEMPERATURE'},
            {id: 'humidity', title: 'HUMIDITY'},
            {id: 'time', title: 'TIME'},
        ],
        append: true
    });

    req.body.time = moment().tz("America/Sao_Paulo").format('DD/MM/YYYY HH:mm:ss');

   csvWriter.writeRecords([req.body])
        .then(() => {
            res.status(200).send('Data written to CSV file');
        })
        .catch((err) => {
            console.error('An error occurred:', err);
            res.status(500).send('An error occurred');
        });

});


app.listen(PORT, () => console.log(`Server is running on port ${PORT}`));