package com.stepanov.uocns.network.traffic;

import com.stepanov.uocns.network.common.TTypeFlit;

public final class TFlitData {
    public TTypeFlit fType;
    public int fSizePacket;
    public int fSizeCore;
    public int fCoreFrom;
    public int fCoreTo;
    public int fVLinkId;
    public int fMinSize;
    public int fClockBorn;
}

