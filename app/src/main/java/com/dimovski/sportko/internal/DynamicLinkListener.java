package com.dimovski.sportko.internal;

import com.google.firebase.dynamiclinks.ShortDynamicLink;

/**Interface that provides method of communication*/
public interface DynamicLinkListener {

    void shortDynamicLinkCreated (ShortDynamicLink shortDynamicLink);
}
