const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);





exports.onOrderChange = functions.database
.ref('/Orders/{orderId}')
.onUpdate((snapshot,context)=>{


     const orderId = context.params.orderId
     const after = snapshot.after.val()
     const before = snapshot.before.val()


      console.log(orderId)
      console.log(before.orderStatus)
      console.log(after.orderStatus)
      console.log(after.managerId)
      admin.database().ref('/User/' + after.managerId).once('value').then(function(snapshot) {
          let token = snapshot.val().Token;
          console.log(token)

          if(after.orderStatus==="Approved"||after.orderStatus==="approved"){
                if(before.orderStatus==="Pending"){
                   console.log("Inside")
                   payload = {
                      notification: {
                         title:"Order : "+orderId,
                         body: "Your order request approved by management. \nPlease check the requests",
                         sound: "default"
                      },
                   };
                    const options = {
                          priority: "high",
                          timeToLive: 60 * 60 * 24
                    };
                   return admin.messaging().sendToDevice(token, payload)
                      .then(function(response) {
                         // See the MessagingDevicesResponse reference documentation for
                         // the contents of response.
                         console.log('Successfully sent message:', response);
                      })
                      .catch(function(error) {
                         console.log('Error sending message:', error);
                   })
                }
         }
     });

})