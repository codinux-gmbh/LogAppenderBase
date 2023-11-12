package net.codinux.log.quarkus;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import net.codinux.log.Logger;
import net.codinux.log.LoggerFactory;

// TODO: move to an extra project like quarkus-log-kmp/klf
@TargetClass(value = LoggerFactory.class)
final class Target_net_codinux_log_LoggerFactory {

    // Required as otherwise for all classes for which getLogger(KClass) gets called @RegisterForReflexion has to be configured.
    // This is the same as Quarkus does for org.slf4j.LoggerFactory.getLogger(Class<?>).
    @Substitute
    public static Logger getLogger(kotlin.reflect.KClass<?> forClass) {
        // TODO: also unwrap companion object
        return LoggerFactory.getLogger(forClass.getQualifiedName());
    }

}
