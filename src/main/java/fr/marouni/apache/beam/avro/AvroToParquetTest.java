/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.marouni.apache.beam.avro;

import fr.marouni.apache.beam.common.GraphVizVisitor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.parquet.ParquetIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import java.io.File;
import java.io.IOException;


public class AvroToParquetTest {

    public static void main(String[] args) throws IOException {

        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();
        Pipeline p = Pipeline.create(options);

        Schema AVRO_SCHEMA =
                new Schema.Parser().parse(new File("/home/abbass/dev/datasets/avro/bitcoin/schema.json"));

        p.apply(AvroIO.readGenericRecords(AVRO_SCHEMA).from("/home/abbass/dev/datasets/avro/bitcoin/*.avro"))
                .apply(FileIO.<GenericRecord>write().via(ParquetIO.sink(AVRO_SCHEMA)).to("/tmp/test_dir"));

        GraphVizVisitor graphVizVisitor = new GraphVizVisitor(p, "/tmp/mypipeviz");
        graphVizVisitor.writeGraph();

        // Run the pipeline.
        p.run().waitUntilFinish();
    }
}
