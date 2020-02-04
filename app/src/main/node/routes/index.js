var express = require('express');
const fs = require ('fs');
var router = express.Router();
const admin = require ('firebase-admin');
const PDFDocument = require('pdfkit');
const serviceAccount = ('./serviceAccountKey.json');
const Excel = require('exceljs');



admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const storage = admin.storage();
const db = admin.firestore();
router.use(express.json());


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

//crea PDF 
router.post('/createPDF', function(req, res, next) {
  const doc = new PDFDocument;
  var bean = req.body;

  var filename = req.body.user_key + '.pdf';
  doc.pipe(fs.createWriteStream(filename));
  console.log('In server...');

  doc.text('UNIVERSITA DEGLI STUDI DI SALERNO, DIPARTIMENTO DI INFORMATICA \n\nAlla Presidente del Consiglio Didattico di Informatica \n'+
  'DOMANDA DI RICONOSCIMENTO DEI CREDITI FORMATIVI PREVISTI PER LA CONOSCENZA DELLA LINGUA INGLESE \n\n'+
  'La/Il sottoscritta/o ' + req.body.user_name +' ' + req.body.user_surname + ' immatricolata/o nell aa ' + req.body.year+ ' ' + 'al corso di Laurea Triennale in Informatica, matricola n°'+
  req.body.matricola + '\n\n                                                               ' + 'CHIEDE\n\nChe venga valutata la certificazione allegata.\n' + 'ENTE CERTIFICATORE: ' + req.body.ente + '\nLIVELLO CEFR: ' +
  req.body.level + '\n' + 'ai fini del riconoscimento di n°' + req.body.validated_cfu + ' ' + 'CFU relativi alla prova di Lingua Inglese previsti nel proprio piano' +
  'di studi.\nSi allega certificazione.\n\n Fisciano, _____________     Firma studente ___________________________');
  console.log(JSON.stringify(bean));
  doc.end();

  storage.bucket("gs://porting-android-is.appspot.com").upload('D:/Documenti/GitHub/ANDROID-IS/app/src/main/node/'+filename,
  function(err, file) {
    if (!err) {
      console.log('File caricato');
      res.send('200');
    }
    else{
      res.send('500');
      console.log(err);
    }
  });
});

//crea Excel richieste approvate
router.post('/createApprovedExcel', function(req, res, next){
  db.collection('request').where('stato', '==', 'Approvata').get()
  .then(snapshot => {
    if(snapshot.empty){
      console.log('Nessun risultato');
      res.send('404');
    }
      var filename = 'Accettate.xlsx';
      const workbook = new Excel.Workbook();
      var cont = 2;
      const worksheet = workbook.addWorksheet('Richieste');
      worksheet.getCell('A1').value = 'EMAIL';
      worksheet.getCell('B1').value = 'NOME';
      worksheet.getCell('C1').value = 'COGNOME';
      snapshot.forEach(doc =>{	     
        worksheet.getCell('A'+cont).value = doc.get('user_key').toString();
        worksheet.getCell('B'+cont).value = doc.get('user_name').toString();
        worksheet.getCell('C'+cont).value = doc.get('user_surname').toString();
        console.log(doc.data());	
        cont = cont + 1;
      })	       
      workbook.xlsx.writeFile(filename)
      .then (function(){
        storage.bucket("gs://porting-android-is.appspot.com").upload('D:/Documenti/GitHub/ANDROID-IS/app/src/main/node/'+filename,
      function(err, file) {
        if (!err) {
          console.log('File caricato');
          res.send('200');
        }
        else{
          res.send('500');
          console.log(err);
        }
      });

      });
      
  })
      
      
  .catch(err =>{
    console.error('Error getting document', err);
    res.send('500');
  })
});

//crea Excel richieste rifiutate
router.post('/createRefusedExcel', function(req, res, next){
  db.collection('request').where('stato', '==', 'Rifiutata').get()
  .then(snapshot => {
    if(snapshot.empty){
      console.log('Nessun risultato');
      res.send('404');
    }
      var filename = 'Rifiutate.xlsx';
      const workbook = new Excel.Workbook();
      var cont = 2;
      const worksheet = workbook.addWorksheet('Richieste');
      worksheet.getCell('A1').value = 'EMAIL'
      worksheet.getCell('B1').value = 'NOME'
      worksheet.getCell('C1').value = 'COGNOME'
      snapshot.forEach(doc =>{	     
        worksheet.getCell('A'+cont).value = doc.get('user_key').toString();
        worksheet.getCell('B'+cont).value = doc.get('user_name').toString();
        worksheet.getCell('C'+cont).value = doc.get('user_surname').toString();
        console.log(doc.data());	
        cont = cont + 1;
      })	       
      
      workbook.xlsx.writeFile(filename)
      .then (function(){
        storage.bucket("gs://porting-android-is.appspot.com").upload('D:/Documenti/GitHub/ANDROID-IS/app/src/main/node/'+filename,
      function(err, file) {
        if (!err) {
          console.log('File caricato');
          res.send('200');
        }
        else{
          res.send('500');
          console.log(err);
        }
      });

      });
      
  })
  .catch(err =>{
    console.error('Error getting document', err);
    res.send('500');
  })
});


module.exports = router;