/*
 * "Copyright (c) 2014   Capgemini Technology Services (hereinafter "Capgemini")
 *
 * License/Terms of Use
 * Permission is hereby granted, free of charge and for the term of intellectual
 * property rights on the Software, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to use, copy, modify and
 * propagate free of charge, anywhere in the world, all or part of the Software
 * subject to the following mandatory conditions:
 *
 * -   The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Any failure to comply with the above shall automatically terminate the license
 * and be construed as a breach of these Terms of Use causing significant harm to
 * Capgemini.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, PEACEFUL ENJOYMENT,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Except as contained in this notice, the name of Capgemini shall not be used in
 * advertising or otherwise to promote the use or other dealings in this Software
 * without prior written authorization from Capgemini.
 *
 * These Terms of Use are subject to French law.
 *
 * IMPORTANT NOTICE: The WUIC software implements software components governed by
 * open source software licenses (BSD and Apache) of which CAPGEMINI is not the
 * author or the editor. The rights granted on the said software components are
 * governed by the specific terms and conditions specified by Apache 2.0 and BSD
 * licenses."
 */


package com.github.wuic.engine.htmlcompressor.test;

import com.github.wuic.engine.htmlcompressor.HtmlCompressorEngine;
import com.github.wuic.ApplicationConfig;
import com.github.wuic.NutType;
import com.github.wuic.config.ObjectBuilder;
import com.github.wuic.config.ObjectBuilderFactory;
import com.github.wuic.engine.Engine;
import com.github.wuic.engine.EngineRequest;
import com.github.wuic.engine.EngineService;
import com.github.wuic.engine.NodeEngine;
import com.github.wuic.nut.ConvertibleNut;
import com.github.wuic.nut.Nut;
import com.github.wuic.nut.NutsHeap;
import com.github.wuic.util.FutureLong;
import com.github.wuic.util.IOUtils;
import com.github.wuic.util.NutUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * HtmlCompressor support test.
 * </p>
 *
 * @author Guillaume DROUET
 * @version 1.0
 * @since 0.5.0
 */
@RunWith(JUnit4.class)
public class HtmlCompressorEngineTest {

    /**
     * <p>
     * Test when compressor is enabled.
     * </p>
     *
     * @throws Exception if test fails
     */
    @Test
    public void htmlCompressTest() throws Exception {
        final Nut nut = Mockito.mock(Nut.class);
        Mockito.when(nut.getNutType()).thenReturn(NutType.HTML);
        Mockito.when(nut.isTextReducible()).thenReturn(true);
        Mockito.when(nut.getInitialName()).thenReturn("index.html");
        Mockito.when(nut.openStream()).thenReturn(HtmlCompressorEngineTest.class.getResourceAsStream("/htmlcompressor/index.html"));
        Mockito.when(nut.getVersionNumber()).thenReturn(new FutureLong(0L));

        final NutsHeap heap = Mockito.mock(NutsHeap.class);
        Mockito.when(heap.getNuts()).thenReturn(Arrays.asList(nut));

        final ObjectBuilderFactory<Engine> factory = new ObjectBuilderFactory<Engine>(EngineService.class, HtmlCompressorEngine.class);
        final ObjectBuilder<Engine> builder = factory.create("HtmlCompressorEngineBuilder");
        final Engine engine = builder.build();

        final List<ConvertibleNut> res = engine.parse(new EngineRequest("wid", "cp", heap, new HashMap<NutType, NodeEngine>()));
        Assert.assertEquals(-1, NutUtils.readTransform(res.get(0)).indexOf('\n'));
    }

    /**
     * <p>
     * Test when compressor is disabled.
     * </p>
     *
     * @throws Exception if test fails
     */
    @Test
    public void disableHtmlCompressTest() throws Exception {
        final Nut nut = Mockito.mock(Nut.class);
        Mockito.when(nut.getNutType()).thenReturn(NutType.HTML);
        Mockito.when(nut.getInitialName()).thenReturn("index.html");
        Mockito.when(nut.openStream()).thenReturn(HtmlCompressorEngineTest.class.getResourceAsStream("/htmlcompressor/index.html"));
        Mockito.when(nut.getVersionNumber()).thenReturn(new FutureLong(0L));

        final NutsHeap heap = Mockito.mock(NutsHeap.class);
        Mockito.when(heap.getNuts()).thenReturn(Arrays.asList(nut));

        final ObjectBuilderFactory<Engine> factory = new ObjectBuilderFactory<Engine>(EngineService.class, HtmlCompressorEngine.class);
        final ObjectBuilder<Engine> builder = factory.create("HtmlCompressorEngineBuilder");
        final Engine engine = builder.property(ApplicationConfig.COMPRESS, false).build();

        final List<ConvertibleNut> res = engine.parse(new EngineRequest("wid", "cp", heap, new HashMap<NutType, NodeEngine>()));
        Assert.assertNotSame(-1, IOUtils.readString(new InputStreamReader(res.get(0).openStream())).indexOf('\n'));
    }
}
