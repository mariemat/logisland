

API design
==========
logisland is a framework that you can extend through its API,
you can use it to build your own ``Processors`` or to build data processing apps over it.


Java API
++++++++
You can extend logisland with the Java low-level API as described below.


The primary material : Records
------------------------------
The basic unit of processing is the Record.
A ``Record`` is a collection of ``Field``, while a ``Field`` has a ``name``, a ``type`` and a ``value``.

You can instanciate a ``Record`` like in the following code snipet:

.. code-block:: java

    String id = "firewall_record1";
    String type = "cisco";
    Record record = new Record(type).setId(id);

    assertTrue(record.isEmpty());
    assertEquals(record.size(), 0);

A record is defined by its type and a collection of fields. there are three special fields:

.. code-block:: java

    // shortcut for id
    assertEquals(record.getId(), id);
    assertEquals(record.getField(FieldDictionary.RECORD_ID).asString(), id);

    // shortcut for time
    assertEquals(record.getTime().getTime(), record.getField(FieldDictionary.RECORD_TIME).asLong().longValue());

    // shortcut for type
    assertEquals(record.getType(), type);
    assertEquals(record.getType(), record.getField(FieldDictionary.RECORD_TYPE).asString());
    assertEquals(record.getType(), record.getField(FieldDictionary.RECORD_TYPE).getRawValue());


And the other fields have generic setters, getters and removers

.. code-block:: java

    record.setStringField("url_host", "origin-www.20minutes.fr")
        .setField("method", FieldType.STRING, "GET")
        .setField("response_size", FieldType.INT, 452)
        .setField("is_outside_office_hours", FieldType.BOOLEAN, false)
        .setField("tags", FieldType.ARRAY, Arrays.asList("spam", "filter", "mail"));

    assertFalse(record.hasField("unkown_field"));
    assertTrue(record.hasField("method"));
    assertEquals(record.getField("method").asString(), "GET");
    assertTrue(record.getField("response_size").asInteger() - 452 == 0);
    assertTrue(record.getField("is_outside_office_hours").asBoolean());
    record.removeField("is_outside_office_hours");
    assertFalse(record.hasField("is_outside_office_hours"));

Fields are strongly typed, you can validate them

.. code-block:: java

    Record record = new StandardRecord();
    record.setField("request_size", FieldType.INT, 1399);
    assertTrue(record.isValid());
    record.setField("request_size", FieldType.INT, "zer");
    assertFalse(record.isValid());
    record.setField("request_size", FieldType.INT, 45L);
    assertFalse(record.isValid());
    record.setField("request_size", FieldType.LONG, 45L);
    assertTrue(record.isValid());
    record.setField("request_size", FieldType.DOUBLE, 45.5d);
    assertTrue(record.isValid());
    record.setField("request_size", FieldType.DOUBLE, 45.5);
    assertTrue(record.isValid());
    record.setField("request_size", FieldType.DOUBLE, 45L);
    assertFalse(record.isValid());
    record.setField("request_size", FieldType.FLOAT, 45.5f);
    assertTrue(record.isValid());
    record.setField("request_size", FieldType.STRING, 45L);
    assertFalse(record.isValid());
    record.setField("request_size", FieldType.FLOAT, 45.5d);
    assertFalse(record.isValid());

The tools to handle processing : Processor
------------------------------------------

logisland is designed as a component centric framework, so there's a layer of abstraction to build configurable components.
Basically a component can be Configurable and Configured.

The most common component you'll use is the ``Processor``

Let's explain the code of a basic ``MockProcessor``, that doesn't acheive a really useful work but which is really self-explanatory
we first need to extend ``AbstractProcessor`` class (or to implement ``Processor`` interface).

.. code-block:: java

    public class MockProcessor extends AbstractProcessor {

        private static Logger logger = LoggerFactory.getLogger(MockProcessor.class);
        private static String EVENT_TYPE_NAME = "mock";

Then we have to define a list of supported ``PropertyDescriptor``. All theses properties and validation stuff are handled by
``Configurable`` interface.

.. code-block:: java

        public static final PropertyDescriptor FAKE_MESSAGE
            = new PropertyDescriptor.Builder()
                .name("fake.message")
                .description("a fake message")
                .required(true)
                .addValidator(StandardPropertyValidators.NON_EMPTY_VALIDATOR)
                .defaultValue("yoyo")
                .build();

        @Override
        public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
            final List<PropertyDescriptor> descriptors = new ArrayList<>();
            descriptors.add(FAKE_MESSAGE);

            return Collections.unmodifiableList(descriptors);
        }


then comes the initialization bloc of the component given a ``ComponentContext`` (more on this later)

.. code-block:: java

    @Override
    public void init(final ComponentContext context) {
        logger.info("init MockProcessor");
    }

And now the real business part with the ``process`` method which handles all the work on the record's collection.

.. code-block:: java

    @Override
    public Collection<Record> process(final ComponentContext context,
                                      final Collection<Record> collection) {
        // log inputs
        collection.stream().forEach(record -> {
            logger.info("mock processing record : {}", record)
        });

        // output a useless record
        Record mockRecord = new Record("mock_record");
        mockRecord.setField("incomingEventsCount", FieldType.INT, collection.size());
        mockRecord.setStringField("message",
                                   context.getProperty(FAKE_MESSAGE).asString());

        return Collections.singleton(mockRecord);
    }


}


The runtime context : Instance
------------------------------
you can use your wonderful processor by setting its configuration and asking the ``ComponentFactory`` to give you one ``ProcessorInstance`` which is a ``ConfiguredComponent``.

.. code-block:: java

    String message = "logisland rocks !";
    Map<String, String> conf = new HashMap<>();
    conf.put(MockProcessor.FAKE_MESSAGE.getName(), message );

    ProcessorConfiguration componentConfiguration = new ProcessorConfiguration();
    componentConfiguration.setComponent(MockProcessor.class.getName());
    componentConfiguration.setType(ComponentType.PROCESSOR.toString());
    componentConfiguration.setConfiguration(conf);

    Optional<StandardProcessorInstance> instance =
        ComponentFactory.getProcessorInstance(componentConfiguration);
    assertTrue(instance.isPresent());

Then you need a ``ComponentContext`` to run your processor.

.. code-block:: java

    ComponentContext context = new StandardComponentContext(instance.get());
    Processor processor = instance.get().getProcessor();

And finally you can use it to process records

.. code-block:: java

    Record record = new Record("mock_record");
    record.setId("record1");
    record.setStringField("name", "tom");
    List<Record> records =
        new ArrayList<>(processor.process(context, Collections.singleton(record)));

    assertEquals(1, records.size());
    assertTrue(records.get(0).hasField("message"));
    assertEquals(message, records.get(0).getField("message").asString());



Chaining processors in a stream : RecordStream
----------------------------------------------

.. warning:: @todo



Running the processor's flow : Engine
-------------------------------------

.. warning:: @todo




Packaging and conf
------------------

The end user of logisland is not the developer, but the business analyst which does understand any line of code.
That's why we can deploy all our components through yaml config files

.. code-block:: yaml

    - processor: mock_processor
      component: com.hurence.logisland.util.runner.MockProcessor
      type: parser
      documentation: a parser that produce events for nothing
      configuration:
         fake.message: the super message



Testing your processors : TestRunner
------------------------------------

When you have coded your processor, pretty sure you want to test it with unit test.
The framework provides you with the ``TestRunner`` tool for that.
All you need is to instantiate a Testrunner with your Processor and its properties.

.. code-block:: java

    final String APACHE_LOG_SCHEMA = "/schemas/apache_log.avsc";
    final String APACHE_LOG = "/data/localhost_access.log";
    final String APACHE_LOG_FIELDS =
        "src_ip,identd,user,record_time,http_method,http_query,http_version,http_status,bytes_out";
    final String APACHE_LOG_REGEX =
        "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+\\[([\\w:/]+\\s[+\\-]\\d{4})\\]\\s+\"(\\S+)\\s+(\\S+)\\s+(\\S+)\"\\s+(\\S+)\\s+(\\S+)";

    final TestRunner testRunner = TestRunners.newTestRunner(new SplitText());
    testRunner.setProperty(SplitText.VALUE_REGEX, APACHE_LOG_REGEX);
    testRunner.setProperty(SplitText.VALUE_FIELDS, APACHE_LOG_FIELDS);
    // check if config is valid
    testRunner.assertValid();

Now enqueue some messages as if they were sent to input Kafka topics

.. code-block:: java

    testRunner.clearQueues();
    testRunner.enqueue(SplitTextTest.class.getResourceAsStream(APACHE_LOG));

Now run the process method and check that every ``Record`` has been correctly processed.

.. code-block:: java

    testRunner.run();
    testRunner.assertAllInputRecordsProcessed();
    testRunner.assertOutputRecordsCount(200);
    testRunner.assertOutputErrorCount(0);

You can validate that all output records are validated against an avro schema

.. code-block:: java

    final RecordValidator avroValidator = new AvroRecordValidator(SplitTextTest.class.getResourceAsStream
    testRunner.assertAllRecords(avroValidator);


And check if your output records behave as expected.

.. code-block:: java

    MockRecord out = testRunner.getOutputRecords().get(0);
    out.assertFieldExists("src_ip");
    out.assertFieldNotExists("src_ip2");
    out.assertFieldEquals("src_ip", "10.3.10.134");
    out.assertRecordSizeEquals(9);
    out.assertFieldEquals(FieldDictionary.RECORD_TYPE, "apache_log");
    out.assertFieldEquals(FieldDictionary.RECORD_TIME, 1469342728000L);







REST API
++++++++
You can extend logisland with the Java high-level REST API as described below.


Design Tools
------------
The REST API is designed with `Swagger <http://swagger.io>`_


You can use the docker image for the swagger-editor to edit the swagger yaml file and generate source code.

.. code-block:: bash

    docker pull swaggerapi/swagger-editor
    docker run -d -p 80:8080 swaggerapi/swagger-editor

If you're under mac you can setup swagger-codegen

.. code-block:: bash

    brew install swagger-codegen

    # or
    wget https://oss.sonatype.org/content/repositories/releases/io/swagger/swagger-codegen-cli/2.2.1/swagger-codegen-cli-2.2.1.jar

You can then start to generate the source code from the swgger yaml file

.. code-block:: bash

    swagger-codegen generate \
        --group-id com.hurence.logisland \
        --artifact-id logisland-agent \
        --artifact-version 0.10.0-SNAPSHOT \
        --api-package com.hurence.logisland.agent.rest.api \
        --model-package com.hurence.logisland.agent.rest.model \
        -o logisland-framework/logisland-agent \
        -l jaxrs \
        --template-dir logisland-framework/logisland-agent/src/main/swagger/templates \
        -i logisland-framework/logisland-agent/src/main/swagger/api-swagger.yaml



Swagger Jetty server
--------------------
This server was generated by the `swagger-codegen <https://github.com/swagger-api/swagger-codegen>`_ project.
By using the `OpenAPI-Spec  <https://github.com/swagger-api/swagger-core/wiki>`_ from a remote server,
you can easily generate a server stub.
This is an example of building a swagger-enabled JAX-RS server.

This example uses the `JAX-RS <http://https://jax-rs-spec.java.net>`_ framework.

To run the server, please execute the following:

.. code-block:: bash

    cd logisland-framework/logisland-agent
    mvn clean package jetty:run

You can then view the `swagger.json  <http://localhost:8080/agent/api/v0.10.0/swagger.json>`_ .

> Note that if you have configured the `host` to be something other than localhost, the calls through
swagger-ui will be directed to that host and not localhost!