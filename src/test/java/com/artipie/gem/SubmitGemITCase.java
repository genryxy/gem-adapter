/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.gem;

import com.artipie.asto.memory.InMemoryStorage;
import com.artipie.http.rs.RsStatus;
import com.artipie.vertx.VertxSliceServer;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.io.IOException;
import java.net.ServerSocket;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * A test for gem submit operation.
 *
 * @since 0.2
 */
public class SubmitGemITCase {

    @Test
    public void submitNotImplemented() throws IOException {
        final Vertx vertx = Vertx.vertx();
        final int port = this.randomPort();
        final VertxSliceServer server = new VertxSliceServer(
            vertx,
            new GemSlice(new InMemoryStorage()),
            port
        );
        final WebClient web = WebClient.create(vertx);
        server.start();
        final int code = web.post(port, "localhost", "/api/v1/gems")
            .rxSendBuffer(Buffer.buffer())
            .blockingGet()
            .statusCode();
        MatcherAssert.assertThat(
            code,
            new IsEqual<>(Integer.parseInt(RsStatus.NOT_IMPLEMENTED.code()))
        );
        web.close();
        server.close();
        vertx.close();
    }

    /**
     * Find a random port.
     * @return A random port.
     * @throws IOException if fails.
     */
    private int randomPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
