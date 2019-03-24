package xyz.brassgoggledcoders.contamination.modules.fertilizer;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teamacronymcoders.base.blocks.IHasBlockStateMapper;
import com.teamacronymcoders.base.client.models.generator.IHasGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.*;
import com.teamacronymcoders.base.util.files.templates.TemplateFile;
import com.teamacronymcoders.base.util.files.templates.TemplateManager;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidBaseFinite extends BlockFluidFinite implements IHasBlockStateMapper, IHasGeneratedModel {
    private String name;

    public BlockFluidBaseFinite(String name, Fluid fluid, Material material) {
        super(fluid, material);
        this.name = name;
        this.setTranslationKey(name);
    }

    @Override
    public String getVariant(IBlockState blockState) {
        return "normal";
    }

    @Override
    public List<IGeneratedModel> getGeneratedModels() {
        TemplateFile templateFile = TemplateManager.getTemplateFile("fluid");
        Map<String, String> replacements = Maps.newHashMap();
        replacements.put("FLUID", this.fluidName);
        templateFile.replaceContents(replacements);

        return Lists.newArrayList(new GeneratedModel(this.getModelPrefix() + name, ModelType.BLOCKSTATE, templateFile.getFileContents()));

    }

    protected String getModelPrefix() {
        return "";
    }

    @Override
    public Block getBlock() {
        return this;
    }
}