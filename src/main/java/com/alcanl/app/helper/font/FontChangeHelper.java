package com.alcanl.app.helper.font;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.awt.*;


@Component
@Lazy
public class FontChangeHelper {
    private int m_index = 12;
    private static final int MAX_FONT_SIZE = 16;
    private static final int MIN_FONT_SIZE = 8;

    public Font getLargerSize()
    {
        if (m_index != MAX_FONT_SIZE)
            m_index += 2;

        return new Font("default", Font.PLAIN, m_index);
    }

    public Font getSmallerSize()
    {
        if (m_index != MIN_FONT_SIZE)
            m_index -= 2;

        return new Font("default", Font.PLAIN, m_index);
    }

}
