package net.haesleinhuepf.clij.clearcl.ops.render.demo;

import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLContext;
import net.haesleinhuepf.clij.clearcl.ClearCLDevice;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackendInterface;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackends;
import net.haesleinhuepf.clij.clearcl.enums.HostAccessType;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.enums.KernelAccessType;
import net.haesleinhuepf.clij.clearcl.enums.MemAllocMode;
import net.haesleinhuepf.clij.clearcl.ops.render.ImageRender;
import net.haesleinhuepf.clij.clearcl.ops.render.enums.Algorithm;
import net.haesleinhuepf.clij.clearcl.util.Region2;
import net.haesleinhuepf.clij.clearcl.viewer.ClearCLImageViewer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;

import org.junit.Test;

/**
 *
 *
 * @author royer
 */
public class ImageRenderDemos
{

  /**
   * Demonstration of max projection volume rendering
   * 
   * @throws InterruptedException
   *           NA
   */
  @Test
  public void demoVolRenderMaxProj() throws InterruptedException
  {
    ClearCLBackendInterface lClearCLBackend =
                                            ClearCLBackends.getBestBackend();

    try (ClearCL lClearCL = new ClearCL(lClearCLBackend))
    {

      ClearCLDevice lBestGPUDevice = lClearCL.getBestGPUDevice();

      System.out.println(lBestGPUDevice.getInfoString());

      ClearCLContext lContext = lBestGPUDevice.createContext();

      int lWidth = 256;
      int lHeight = 256;
      int lDepth = 256;

      ClearCLImage l3DImage =
                            lContext.createSingleChannelImage(HostAccessType.WriteOnly,
                                                              KernelAccessType.ReadWrite,
                                                              ImageChannelDataType.UnsignedInt8,
                                                              lWidth,
                                                              lHeight,
                                                              lDepth);

      final byte[] lVolumeDataArray = new byte[lWidth * lHeight
                                               * lDepth];

      for (int z = 0; z < lDepth; z++)
        for (int y = 0; y < lHeight; y++)
          for (int x = 0; x < lWidth; x++)
          {
            final int lIndex = x + lWidth * y + lWidth * lHeight * z;
            int lCharValue = (((byte) x ^ (byte) y ^ (byte) z));
            if (lCharValue < 12)
              lCharValue = 0;
            lVolumeDataArray[lIndex] = (byte) lCharValue;
          }

      l3DImage.readFrom(lVolumeDataArray, true);

      ClearCLBuffer lRGBABuffer =
                                lContext.createBuffer(MemAllocMode.AllocateHostPointer,
                                                      HostAccessType.ReadOnly,
                                                      KernelAccessType.WriteOnly,
                                                      4,
                                                      NativeTypeEnum.Byte,
                                                      Region2.region(l3DImage.getDimensions()));

      ImageRender lImageRender =
                               new ImageRender(lContext.getDefaultQueue(),
                                               Algorithm.MaximumProjection);

      lImageRender.render(l3DImage, lRGBABuffer, true);

      ClearCLImageViewer lView = ClearCLImageViewer.view(lRGBABuffer);

      for (int i = 0; i < 100 && lView.isShowing(); i++)
      {
        Thread.sleep(10);
      }

    }
  }

}
