/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.nui.layers.mainMenu.videoSettings;

import org.terasology.config.RenderingConfig;
import org.terasology.rendering.nui.databinding.Binding;

public class ScreenshotSizeBinding implements Binding<ScreenshotSize> {

    private RenderingConfig config;

    public ScreenshotSizeBinding(RenderingConfig config) {
        this.config = config;
    }

    @Override
    public ScreenshotSize get() {
        if (config.getScreenshotSize() == 0) {
            return ScreenshotSize.SUPER;
        } else if (config.getScreenshotSize() == 1) {
            return ScreenshotSize.NORMAL;
        } else if (config.getScreenshotSize() == 2) {
            return ScreenshotSize.SMALL;
        } else if (config.getScreenshotSize() == 3) {
            return ScreenshotSize.THUMBNAIL;
        } else {
            return ScreenshotSize.NORMAL;
        }
    }

    @Override
    public void set(ScreenshotSize value) {
        value.apply(config);
    }
}
