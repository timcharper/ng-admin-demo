var myApp = angular.module('myApp', ['ng-admin']);
myApp.config(['NgAdminConfigurationProvider', function(NgAdminConfigurationProvider) {
  var nga = NgAdminConfigurationProvider;
  // create an admin application
  var admin = nga.application('My First Admin');
  // more configuation here later
  // ...
  // attach the admin application to the DOM and run it
  nga.configure(admin);



  // the API endpoint for this entity will be 'http://jsonplaceholder.typicode.com/users/:id
  var user = nga.entity('users').baseApiUrl('/v1/');
  // set the fields of the user entity list view
  user.listView().
    fields([
      nga.field('id'),
      nga.field('email'),
      nga.field('first_name'),
      nga.field('last_name')
    ]).
    listActions(['show', 'delete']);
  
  user.showView().fields([
    nga.field('id'),
    nga.field('email'),
    nga.field('first_name'),
    nga.field('last_name'),
    nga.field('last_login', 'datetime'),
    nga.field('created_at', 'datetime')
  ]);

  user.editionView().fields([
    nga.field('email', 'email'),
    nga.field('first_name'),
    nga.field('last_name')
  ]);

  user.creationView().fields([
    nga.field('email', 'email'),
    nga.field('first_name'),
    nga.field('last_name'),
    nga.field('password', 'password')
  ]);
  // add the user entity to the admin application
  admin.addEntity(user);





  var bucket = nga.entity('buckets').baseApiUrl('/v1/');
  bucket.listView().
    fields([
      nga.field('id'), //: UUID,
      nga.field('modified', 'datetime'), //: ZonedDateTime,
      nga.field('created', 'datetime'), //: ZonedDateTime,
      nga.field('user_id') //: Int
    ]).
    listActions(['show', 'delete']);

  bucket.showView().fields([
    nga.field('id'), //: UUID,
    nga.field('created', 'datetime'), //: ZonedDateTime,
    nga.field('modified', 'datetime'), //: ZonedDateTime,
    nga.field('name'), //: String,
    nga.field('user_id', 'reference')
      .targetEntity(user)
      .targetField(nga.field('email'))
      .label('User')
  ]);
  
  bucket.creationView().fields([
    nga.field('name'), //: String,
    nga.field('user_id', 'reference')
      .targetEntity(user)
      .targetField(nga.field('email'))
      .label('User')
  ]);
  admin.addEntity(bucket);
  
  // attach the admin application to the DOM and execute it
  nga.configure(admin);
}]);
