## OWT-7: Database communication in Karaf via services

Table of Contents
=================
# [TOC]


## 1. General Notes

Hibernate ORM (Hibernate in short) is an object-relational mapping tool for the Java programming language. It provides a framework for mapping an object-oriented domain model to a relational database. Hibernate handles object-relational impedance mismatch problems by replacing direct, persistent database accesses with high-level object handling functions.

The purpose of this tutorial is to showcase database communication in an OSGi service. In particular it will:
1. Enhance the service(s) used in OWT-6, and integrate Hibernate into them.
2. Store and read data from an underlying database.


>The code for this tutorial is available in the `code` directory. It is recommended to have it checked-out locally and refer to it while going through the tutorial.  
>All the maven modules detailed below have the standard maven directory structure.  
>The code of this tutorial builds on the base provided by OWT-5. As a result, only new concepts/features will be explained here.


> #### Cleaning Karaf
> In order to avoid dependency conflicts and other configuration errors, we will make sure that Karaf is clean and unnecessary bundles are removed. _(Please note that this is not mandatory, but it is considered a good practice)_.
>
> This is achieved by starting Karaf the following way (for Windows):
> ```
> karaf.bat clean
> ```


## 2. Maven Module: bundle-parent
As in previous OWT tutorials, the `bundle-parent` maven module serves as the _parent_ maven project for building the application/service.

## 3. Maven Module: bundle-lib
Library bundle. No changes from OWT-5.


## 4. Maven Module: bundle-api
The Services container bundle. A new service spesification has been added. The `EchoServiceDao` which will handle the data communitcation with the database. 

```java
	Integer create(MessageDTO messageDTO);
	MessageDTO get(Integer id);
	void update(Integer id);
```



 ## 5. Maven Module: bundle-impl
The Service implementation module. 

### Connecting the services
The implementation of EchoService must communicate with the database via a new OSGi service which will handle that interaction.
To be able to call the new OSGi service, it must be injected properly:
	```java
    //...
    @Inject
	@OsgiService
	private EchoServiceDao echoServiceDao;	
   /// ...
```




 ## 6. Maven Module: bundle-rest

This bundle hosts the REST service. No changes from OWT-6.

## 7. Maven Module: bundle-ui

This bundle hosts the Angular web application. No changes from OWT-6.


## 8. Maven Module: bundle-db

This bundle contains all the database interaction modules.



### Persistence-speciﬁc instructions


A persistence bundle is an OSGi bundle that contains one or more persistence descriptors (persistence.xml files) and has a Meta-Persistence header in the bundle manifest, META-INF/MANIFEST.MF. This header lists all the locations of persistence.xml files in the persistence bundle. When this header is present, the default location, `META-INF/persistence.xml`, is added by default. Therefore, when the persistence.xml files are in the default location, the Meta-Persistence header must be present, but its content can be empty (a single space). The following example of a Meta-Persistence header defines a persistence bundle in entities. It could have more than one peristant `Meta-Persistence` entries though. Any persistence.xml files that are in the default location can also be used.
In OSGi, `DynamicImport-Package` attribute is used in the MANIFEST.MF file to specify the patterns of packages that are not found in the normal bundle contents or Import-Package field. If the package is not available in the initial resolution process, it will not fail, but will be attempted to resolve every time a class from the package is required. In this case `hibernate` and `javassist` packages are included.
In order not to enter manually the mandatory entries to the MANIFEST.MF, `org.apache.felix maven-bundle-plugin` can be used for declaring these entries at pom.xml:

```xml
<!-- ... -->
<Meta-Persistence>META-INF/persistence.xml</Meta-Persistence>
<DynamicImport-Package>org.hibernate,org.hibernate.proxy,javassist.util.proxy</ DynamicImport-Package>
<!-- ... -->
```


### persistence.xml

The persistence.xml file is a standard configuration file in JPA. It has to be included in the META-INF directory inside the JAR file that contains the entity beans. The persistence.xml file must define a persistence-unit with a unique name in the current scoped classloader. The provider attribute specifies the underlying implementation of the JPA EntityManager. In JBoss AS, the default and only supported / recommended JPA provider is Hibernate. The jta-data-source points to the JNDI name of the database this persistence unit maps to






### Entities


An entity is a lightweight persistence domain object. Typically an entity represents a table in a relational database, and each entity instance corresponds to a row in that table. The primary programming artifact of an entity is the entity class, although entities can use helper classes.

The persistent state of an entity is represented either through persistent fields or persistent properties. These fields or properties use object/relational mapping annotations to map the entities and entity relationships to the relational data in the underlying data store.

Requirements for Entity Classes
An entity class must follow these requirements:

The class must be annotated with the javax.persistence.Entity annotation.

The class must have a public or protected, no-argument constructor. The class may have other constructors.

The class must not be declared final. No methods or persistent instance variables must be declared final.

If an entity instance be passed by value as a detached object, such as through a session bean’s remote business interface, the class must implement the Serializable interface.

Entities may extend both entity and non-entity classes, and non-entity classes may extend entity classes.

Persistent instance variables must be declared private, protected, or package-private, and can only be accessed directly by the entity class’s methods. Clients must access the entity’s state through accessor or business methods.


EchoServiceDaoImpl



### Manifest

##################################
other
##################################



# Apache Aries
" The Aries project will deliver a set of pluggable Java components enabling an enterprise OSGi application programming model. This includes implementations and extensions of applicationfocused speciﬁcations deﬁned by the OSGi Alliance Enterprise Expert Group (EEG) and an assembly format for multi-bundle applications, for deployment to a variety of OSGi based runtimes. "



 Enable the features

      karaf@root()> `feature:install jpa transaction jndi jdbc pax-jdbc pax-jdbc-pool-dbcp2 pax-jdbc-config hibernate `

Install the database handler of your choice


karaf@root()> feature:install pax-jdbc-h2


# configure the datasource :
  
    config:edit + "darasource configuration name"



```
karaf@root()> config:edit org.ops4j.datasource-OWT7;  
config:property-set osgi.jdbc.driver.name H2;
config:property-set databaseName test;
config:property-set user sa;
config:property-set dataSourceName owt7-ds;
config:update 

```
check log in parallel... and wait.. 



## JDBC registration test
 jdbc:ds-list 


 ![](img/.png)

# JNDI registration test

since we wil use jpa , we actually  do a jndi-lookup :

 jndi:names 

#jdbc test query
jdbc:query owt7-ds select 1 


# ta services sto osgi mporoun na ginoun queried apo ena ldap like interface


service:list javax.sql.DataSource

// list me all the services that implemt javax.sql.DataSource

```
 databaseName = test
 dataSourceName = owt7-ds
 felix.fileinstall.filename = file:/D:/Servers/apache-karaf-4.0.7/etc/org.ops4j.datasource-OWT7.cfg
 osgi.jdbc.driver.name = H2
 osgi.jndi.service.name = owt7-ds
 service.bundleid = 55
 service.factoryPid = org.ops4j.datasource
 service.id = 173
 service.pid = org.ops4j.datasource.66aaccb0-b7ec-4c18-83a4-19e7e176f6be
 service.scope = singleton
 user = sa
Provided by :
 OPS4J Pax JDBC Config (55)
Used by:
 Apache Karaf :: JDBC :: Core (85)
 Apache Karaf :: JNDI :: Core (86)
```
 ![](img/.png)

` osgi.jndi.service.name = owt7-ds` to pio shmantiko 




# auto to service to dhmiourghse to pax-jdbc config... !!

```xml


```


# bundle-db


creating the bundle-db:

adding perisistance xml...

creating the model class. ChatUser


gemizoume to entity me data ..

To add entity manager at our impl:


   @PersistenceContext(unitName = "owt7PU") // # SOS!!! dinoume to unit name pou eixame dwsei sto peristance.xml..
// to perisit den 8a ginei sto impl!
alla sto db.. 8a ftia3oume ena service sto 



# bundle-api..
grafw to declaraiton tou EchoServiceDao


# bundle-impl..
we import the entity
 
we change the EchoServiceImpl.. to perisist the data...




   
    private EntityManager em;

// shows all tables.. 
 jdbc:query owt7-ds SHOW TABLES

 jdbc:query owt7-ds select * from messagechat


##################################

