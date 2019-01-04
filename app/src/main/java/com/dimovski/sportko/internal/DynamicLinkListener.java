package com.dimovski.sportko.internal;

import com.google.firebase.dynamiclinks.ShortDynamicLink;

public interface DynamicLinkListener {

    void shortDynamicLinkCreated (ShortDynamicLink shortDynamicLink);
}
