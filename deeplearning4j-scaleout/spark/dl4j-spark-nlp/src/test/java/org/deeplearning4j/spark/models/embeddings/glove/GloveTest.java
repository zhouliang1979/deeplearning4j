/*
 * Copyright 2015 Skymind,Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.deeplearning4j.spark.models.embeddings.glove;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.glove.GloveWeightLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.spark.text.BaseSparkTest;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Created by agibsonccc on 1/31/15.
 */
public class GloveTest extends BaseSparkTest {

    @Test
    public void testGlove() throws Exception {
        Glove glove = new Glove(true,5,100);
        JavaRDD<String> corpus = sc.textFile(new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath()).map(new Function<String, String>() {
            @Override
            public String call(String s) throws Exception {
                return s.toLowerCase();
            }
        }).cache();


        Pair<VocabCache,GloveWeightLookupTable> table = glove.train(corpus);
        WordVectors vectors = WordVectorSerializer.fromPair(new Pair<>((WeightLookupTable) table.getSecond(), table.getFirst()));
        Collection<String> words = vectors.wordsNearest("day", 20);
        assertTrue(words.contains("week"));
    }

}
