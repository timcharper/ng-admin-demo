# Booting

You need a postgres instance, running locally. Create the database
`ngadmin` with user `ngadmin`, password `ngadmin`, root user.

    CREATE USER 'ngadmin' WITH PASSWORD 'ngadmin' SUPERUSER;

Then, start the project:

    sbt run

If you've defined your permissions appropriately, it will define the
postgres schema on boot.

Open http://localhost:8080/ui/

# Potential Improvements

Having the individual crud schemas is helpful in constraining what can
go where, and communicating which fields are required. Maintaining
them might be tedious. We could, instead, have a single mutator schema
with many of the fields optional, and then use run-time constraints to
prevent certain fields from being set.

We could use a whitebox macro to generate the readview, and remove
fields dynamically that should not be present.

