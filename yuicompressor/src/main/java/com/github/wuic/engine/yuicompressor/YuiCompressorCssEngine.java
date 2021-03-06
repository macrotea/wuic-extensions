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

package com.github.wuic.engine.yuicompressor;

import com.github.wuic.ApplicationConfig;
import com.github.wuic.NutType;
import com.github.wuic.config.BooleanConfigParam;
import com.github.wuic.config.ConfigConstructor;
import com.github.wuic.config.IntegerConfigParam;
import com.github.wuic.config.StringConfigParam;
import com.github.wuic.engine.EngineService;
import com.github.wuic.engine.EngineType;
import com.github.wuic.engine.core.AbstractCompressorEngine;
import com.github.wuic.nut.ConvertibleNut;
import com.github.wuic.util.IOUtils;
import com.yahoo.platform.yui.compressor.CssCompressor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * This class can compress CSS files using the YUI compressor library.
 * </p>
 * 
 * @author Guillaume DROUET
 * @version 1.6
 * @since 0.1.0
 */
@EngineService(injectDefaultToWorkflow = true)
public class YuiCompressorCssEngine extends AbstractCompressorEngine {

    /**
     * Charset of processed files.
     */
    private String charset;

    /**
     * Position of break line (-1 if not \n should be inserted).
     */
    private Integer lineBreakPos;

    /**
     * <p>
     * Builds a new instance.
     * </p>
     *
     * @param compress activate compression or not
     * @param cs the char set
     * @param lbp the line break position
     */
    @ConfigConstructor
    public YuiCompressorCssEngine(
            @BooleanConfigParam(propertyKey = ApplicationConfig.COMPRESS, defaultValue = true) final Boolean compress,
            @StringConfigParam(propertyKey = ApplicationConfig.CHARSET, defaultValue = "UTF-8") final String cs,
            @IntegerConfigParam(propertyKey = ApplicationConfig.LINE_BREAK_POS, defaultValue = -1) final Integer lbp) {
        super(compress, ".min");
        charset = cs;
        lineBreakPos = lbp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transform(final InputStream source, final OutputStream target, final ConvertibleNut convertibleNut)
            throws IOException {
        Reader in = null;
        Writer out = null;
        
        try {
            // Stream to read from the source
            in = new InputStreamReader(source, charset);
     
            // Create the compressor using the source stream
            final CssCompressor compressor = new CssCompressor(in);
            
            // Now close the stream read
            in.close();
            in = null;
            
            // Stream to write into the target
            out = new OutputStreamWriter(target, charset);
            
            // Compress the script into the output target
            compressor.compress(out, lineBreakPos);
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NutType> getNutTypes() {
        return Arrays.asList(NutType.CSS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineType getEngineType() {
        return EngineType.MINIFICATION;
    }
}
