package com.google.ads.mediation;

import com.google.ads.mediation.MediationServerParameters;

@Deprecated
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public interface MediationAdapter<ADDITIONAL_PARAMETERS, SERVER_PARAMETERS extends MediationServerParameters> {
    void destroy();

    Class<ADDITIONAL_PARAMETERS> getAdditionalParametersType();

    Class<SERVER_PARAMETERS> getServerParametersType();
}
