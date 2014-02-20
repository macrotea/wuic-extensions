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


package com.github.wuic.nut.ssh;

import com.github.wuic.ApplicationConfig;
import com.github.wuic.exception.BuilderPropertyNotSupportedException;
import com.github.wuic.nut.AbstractNutDaoBuilder;
import com.github.wuic.nut.NutDao;

import com.github.wuic.nut.setter.*;
import com.github.wuic.nut.setter.ProxyUrisPropertySetter;

/**
 * <p>
 * Builder for nut access on a FTP server.
 * </p>
 *
 * @author Guillaume DROUET
 * @version 1.3
 * @since 0.3.1
 */
public class SshNutDaoBuilder extends AbstractNutDaoBuilder {

    /**
     * Default SSH port.
     */
    private static final int DEFAULT_PORT = 22;

    /**
     * <p>
     * Creates a new instance.
     * </p>
     */
    public SshNutDaoBuilder() {
        super();
        addPropertySetter(new RegexPropertySetter(this),
                new BasePathPropertySetter(this, "."),
                new BasePathAsSysPropPropertySetter(this, false),
                new ProxyUrisPropertySetter(this, null),
                new PollingInterleavePropertySetter(this, -1),
                new ServerDomainPropertySetter(this, "localhost"),
                new ServerPortPropertySetter(this, DEFAULT_PORT),
                new LoginPropertySetter(this, null),
                new PasswordPropertySetter(this, null),
                new ContentBasedVersionNumberPropertySetter(this));
    }

    /**
     * {@inheritDoc}
     */
    public NutDao internalBuild() throws BuilderPropertyNotSupportedException {
        return new SshNutDao((Boolean) property(ApplicationConfig.REGEX),
                (String) property(ApplicationConfig.SERVER_DOMAIN),
                (Integer) property(ApplicationConfig.SERVER_PORT),
                (String) property(ApplicationConfig.BASE_PATH),
                (Boolean) property(ApplicationConfig.BASE_PATH_AS_SYS_PROP),
                (String) property(ApplicationConfig.LOGIN),
                (String) property(ApplicationConfig.PASSWORD),
                (String[]) property(ApplicationConfig.PROXY_URIS),
                (Integer) property(ApplicationConfig.POLLING_INTERLEAVE),
                (Boolean) property(ApplicationConfig.CONTENT_BASED_VERSION_NUMBER));
    }
}