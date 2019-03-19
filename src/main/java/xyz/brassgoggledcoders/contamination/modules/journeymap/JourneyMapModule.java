package xyz.brassgoggledcoders.contamination.modules.journeymap;

import java.util.List;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;

import xyz.brassgoggledcoders.contamination.ContaminationMod;

@Module(ContaminationMod.MODID)
public class JourneyMapModule extends ModuleBase {
    @Override
    public String getName() {
        return "JourneyMap";
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("journeymap"));
        return super.getDependencies(dependencies);
    }
}
