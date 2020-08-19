package com.Netty.NIO.config;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import static io.netty.util.internal.ObjectUtil.checkPositive;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.ObjectUtil;

import java.util.List;

/**
 * A decoder that splits the received {@link ByteBuf}s by one or more
 * delimiters.  It is particularly useful for decoding the frames which ends
 * with a delimiter such as {@link Delimiters#nulDelimiter() NUL} or
 * {@linkplain Delimiters#lineDelimiter() newline characters}.
 *  一种解码器，将接收到的{@link ByteBuf}拆分一个或多个分词器。 这对于解码以分隔符（例如{@link Delimiters＃nulDelimiter（）NUL}或{@linkplain Delimiters＃lineDelimiter（）newline character}）结尾的帧特别有用。
 *
 * <h3>Predefined delimiters</h3>   预定义的分隔符
 * <p>
 * {@link Delimiters} defines frequently used delimiters for convenience' sake. 为方便起见，定义了常用的分词器。
 *
 * <h3>Specifying more than one delimiter</h3>
 * <p>
 * {@link io.netty.handler.codec.DelimiterBasedFrameDecoder} allows you to specify more than one
 * delimiter.  If more than one delimiter is found in the buffer, it chooses
 * the delimiter which produces the shortest frame.  For example, if you have
 * the following data in the buffer:  允许您指定多个分词器。 如果在缓冲区中找到多个分词器，它将选择产生最短帧的分词器。 例如，如果缓冲区中包含以下数据：
 * <pre>
 * +--------------+
 * | ABC\nDEF\r\n |
 * +--------------+
 * </pre>
 * a {@link io.netty.handler.codec.DelimiterBasedFrameDecoder}({@link Delimiters#lineDelimiter() Delimiters.lineDelimiter()})
 * will choose {@code '\n'} as the first delimiter and produce two frames:  将选择{@code'\ n'}作为第一个分词器，并产生两个帧：
 * <pre>
 * +-----+-----+
 * | ABC | DEF |
 * +-----+-----+
 * </pre>
 * rather than incorrectly choosing {@code '\r\n'} as the first delimiter:  而不是错误地选择{@code'\ r \ n'}作为第一个分词器：
 * <pre>
 * +----------+
 * | ABC\nDEF |
 * +----------+
 * </pre>
 */
public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {

    private final ByteBuf[] delimiters; // 分词ByteBuf数组 分词器
    // 最大帧长
    private final int maxFrameLength;
    // 带分隔符
    private final boolean stripDelimiter;
    // 快速失败
    private final boolean failFast;
    // 丢弃太长的帧
    private boolean discardingTooLongFrame;
    // 帧长太长
    private int tooLongFrameLength;
    /** Set only when decoding with "\n" and "\r\n" as the delimiter.  */
    // 仅在使用“ \ n”和“ \ r \ n”作为分隔符进行解码时设置
    private final LineBasedFrameDecoder lineBasedDecoder;

    /**
     * Creates a new instance. 创建一个新实例。
     *
     * @param maxFrameLength  the maximum length of the decoded frame. 解码帧的最大长度。
     *                        A {@link TooLongFrameException} is thrown if the length of the frame exceeds this value. 如果帧的长度超过此值，则会引发{@link TooLongFrameException}。
     * @param delimiter  the delimiter   一个分词器
     */
    public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf delimiter) {
        this(maxFrameLength, true, delimiter);
    }

    /**
     * Creates a new instance.  创建一个新实例
     *
     * @param maxFrameLength  the maximum length of the decoded frame.  解码帧的最大长度。
     *                        A {@link TooLongFrameException} is thrown if the length of the frame exceeds this value.  如果帧的长度超过此值，则会引发{@link TooLongFrameException}。
     * @param stripDelimiter  whether the decoded frame should strip out the delimiter or not   解码后的帧是否应该去除分词器
     * @param delimiter  the delimiter   一个分词器
     */
    public DelimiterBasedFrameDecoder(
            int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        this(maxFrameLength, stripDelimiter, true, delimiter);
    }

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength  the maximum length of the decoded frame. A {@link TooLongFrameException} is thrown if the length of the frame exceeds this value.
     *                        解码帧的最大长度。 如果帧的长度超过此值，则会引发{@link TooLongFrameException}。
     * @param stripDelimiter  whether the decoded frame should strip out the delimiter or not   解码后的帧是否应该去除分词器
     * @param failFast  If <tt>true</tt>, a {@link TooLongFrameException} is thrown as soon as the decoder notices the length of the frame will exceed <tt>maxFrameLength</tt> regardless of whether the entire frame has been read. If <tt>false</tt>, a {@link TooLongFrameException} is thrown after the entire frame that exceeds <tt>maxFrameLength</tt> has been read.
     *                  则一旦解码器注意到帧的长度将超过<tt> maxFrameLength </ tt>，则无论是否已整个帧，都将引发{@link TooLongFrameException} 读。
     *                  如果<tt> false </ tt>，则在读取超过<tt> maxFrameLength </ tt>的整个帧之后，将引发{@link TooLongFrameException}。
     * @param delimiter  the delimiter  一个分词器
     */
    public DelimiterBasedFrameDecoder(
            int maxFrameLength, boolean stripDelimiter, boolean failFast,
            ByteBuf delimiter) {
        this(maxFrameLength, stripDelimiter, failFast, new ByteBuf[] {
                delimiter.slice(delimiter.readerIndex(), delimiter.readableBytes())});
    }

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength  the maximum length of the decoded frame.
     *                        A {@link TooLongFrameException} is thrown if
     *                        the length of the frame exceeds this value.
     * @param delimiters  the delimiters
     */
    public DelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf... delimiters) {
        this(maxFrameLength, true, delimiters);
    }

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength  the maximum length of the decoded frame.
     *                        A {@link TooLongFrameException} is thrown if
     *                        the length of the frame exceeds this value.
     * @param stripDelimiter  whether the decoded frame should strip out the
     *                        delimiter or not
     * @param delimiters  the delimiters
     */
    public DelimiterBasedFrameDecoder(
            int maxFrameLength, boolean stripDelimiter, ByteBuf... delimiters) {
        this(maxFrameLength, stripDelimiter, true, delimiters);
    }

    /**
     * Creates a new instance.
     *
     * @param maxFrameLength    最大长度，超过抛出异常
     * @param stripDelimiter    是否截取掉分词器
     * @param failFast      则一旦解码器注意到帧的长度将超过 maxFrameLength，将会立刻引发异常，否则，将在读取整个帧之后，引发异常
     * @param delimiters    分词器
     *                    真正起作用的构造函数
     */
    public DelimiterBasedFrameDecoder(
            int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiters) {
        validateMaxFrameLength(maxFrameLength);
        // 监测定界符是否为空，如果为空则抛出异常 检查给定参数严格为正，如果不为正，抛出异常
        ObjectUtil.checkNonEmpty(delimiters, "delimiters");
        // 当分词器是 \n 或者 \r\n 的时候返回true ，或者该类的实例是子类
        if (isLineBased(delimiters) && !isSubclass()) {
            lineBasedDecoder = new LineBasedFrameDecoder(maxFrameLength, stripDelimiter, failFast);  // 创建LineBasedFrameDecoder 并设置值
            this.delimiters = null;
        } else {
            this.delimiters = new ByteBuf[delimiters.length];
            for (int i = 0; i < delimiters.length; i ++) {
                ByteBuf d = delimiters[i];
                // 如果 delimiters是只读模式，抛出异常
                validateDelimiter(d);
                this.delimiters[i] = d.slice(d.readerIndex(), d.readableBytes());
            }
            lineBasedDecoder = null;
        }
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.failFast = failFast;
    }

    /** Returns true if the delimiters are "\n" and "\r\n".  */
    // 如果分隔符是 \ n”或者“ \ r \ n 返回true
    private static boolean isLineBased(final ByteBuf[] delimiters) {
        if (delimiters.length != 2) {
            return false;
        }
        ByteBuf a = delimiters[0];
        ByteBuf b = delimiters[1];
        if (a.capacity() < b.capacity()) {
            a = delimiters[1];
            b = delimiters[0];
        }
        return a.capacity() == 2 && b.capacity() == 1
                && a.getByte(0) == '\r' && a.getByte(1) == '\n'
                && b.getByte(0) == '\n';
    }

    /**
     * Return {@code true} if the current instance is a subclass of DelimiterBasedFrameDecoder
     */
    private boolean isSubclass() {
        Class<?> clazz = getClass();
        return clazz == io.netty.handler.codec.DelimiterBasedFrameDecoder.class;
    }

    @Override
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    /**
     * Create a frame out of the {@link ByteBuf} and return it.
     *
     * @param   ctx             the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param   buffer          the {@link ByteBuf} from which to read data
     * @return  frame           the {@link ByteBuf} which represent the frame or {@code null} if no frame could
     *                          be created.
     */
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        if (lineBasedDecoder != null) {
//            return lineBasedDecoder.decode(ctx, buffer);
            // 因为这个方法是protected的，所以我复制出来的方法不能直接引用，这个方法是，如果\n 或者 \r\n 作为分词器的时候，调用 lineBasedDecoder 分词器进行分词
        }
        // Try all delimiters and choose the delimiter which yields the shortest frame.
        // 尝试所有分词器，然后选择产生最短帧的分词器
        int minFrameLength = Integer.MAX_VALUE;
        ByteBuf minDelim = null;
        for (ByteBuf delim: delimiters) {
            int frameLength = indexOf(buffer, delim);
            if (frameLength >= 0 && frameLength < minFrameLength) {
                minFrameLength = frameLength;
                minDelim = delim;
            }
        }

        if (minDelim != null) {
            // 返回此缓冲区可以包含的字节数（八位字节）。
            int minDelimLength = minDelim.capacity();
            ByteBuf frame;
            // 帧长度太长，抛出异常
            if (discardingTooLongFrame) {
                // We've just finished discarding a very large frame.
                // Go back to the initial state.
                discardingTooLongFrame = false;
                buffer.skipBytes(minFrameLength + minDelimLength);

                int tooLongFrameLength = this.tooLongFrameLength;
                this.tooLongFrameLength = 0;
                if (!failFast) {
                    fail(tooLongFrameLength);
                }
                return null;
            }
            // 帧长度大于分词器下表的大小
            if (minFrameLength > maxFrameLength) {
                // Discard read frame.  丢弃读框。
                buffer.skipBytes(minFrameLength + minDelimLength);
                fail(minFrameLength);
                return null;
            }
            // 构造函数传入的值，如果传入true则在分词的时候去除掉分词器，否则，保留分词器
            if (stripDelimiter) {
                // 从当前的{@code readerIndex}返回此缓冲区的子区域的新保留切片，并将{@code readerIndex}增加新切片的大小（= {@code length}）。
                // 说白了就是根据 minFrameLength 切割 buffer，将切割后的buffer返回
                frame = buffer.readRetainedSlice(minFrameLength);
                /// 在此缓冲区中将当前{@code readerIndex}增加指定的{@code length}。
                buffer.skipBytes(minDelimLength);
            } else {
                frame = buffer.readRetainedSlice(minFrameLength + minDelimLength);
            }

            return frame;
        } else {
            if (!discardingTooLongFrame) {
                if (buffer.readableBytes() > maxFrameLength) {
                    // Discard the content of the buffer until a delimiter is found.
                    tooLongFrameLength = buffer.readableBytes();
                    buffer.skipBytes(buffer.readableBytes());
                    discardingTooLongFrame = true;
                    if (failFast) {
                        fail(tooLongFrameLength);
                    }
                }
            } else {
                // Still discarding the buffer since a delimiter is not found.
                tooLongFrameLength += buffer.readableBytes();
                // skipBytes 在此缓冲区中将当前的{@code readerIndex}增加指定的{@code length}。
                buffer.skipBytes(buffer.readableBytes());
            }
            return null;
        }
    }
// 失败
    private void fail(long frameLength) {
        if (frameLength > 0) {
            throw new TooLongFrameException(
                    "frame length exceeds " + maxFrameLength +
                            ": " + frameLength + " - discarded");
        } else {
            throw new TooLongFrameException(
                    "frame length exceeds " + maxFrameLength +
                            " - discarding");
        }
    }

    /**
     * Returns the number of bytes between the readerIndex of the haystack and  the first needle found in the haystack.  -1 is returned if no needle is  found in the haystack.
     * 返回在 haystack 的readerIndex和在 haystack 中找到的第一个指针之间的字节数。 如果在 haystack 中找不到针，则返回-1。
     */
    private static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i ++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex ++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                    break;
                } else {
                    haystackIndex ++;
                    if (haystackIndex == haystack.writerIndex() &&
                            needleIndex != needle.capacity() - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.capacity()) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }

    private static void validateDelimiter(ByteBuf delimiter) {
        ObjectUtil.checkNotNull(delimiter, "delimiter");
        if (!delimiter.isReadable()) {
            throw new IllegalArgumentException("empty delimiter");
        }
    }

    private static void validateMaxFrameLength(int maxFrameLength) {
        checkPositive(maxFrameLength, "maxFrameLength");
    }
}
