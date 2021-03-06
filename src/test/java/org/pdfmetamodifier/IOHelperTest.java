/*
 * Copyright (c) 2012-2016 PdfMetaModifier Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * This file is part of PdfMetaModifier.
 */
package org.pdfmetamodifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.CRC32;

import org.junit.Test;

/**
 * Test for {@link IOHelper}.
 * 
 * @author Dmitry Zavodnikov (d.zavodnikov@gmail.com)
 */
public class IOHelperTest {

    private final static String TEST_PATH = "src" + File.separatorChar + "test" + File.separatorChar + "resources";

    private static long fileCRC32(final File file) throws IOException {
        final CRC32 crc32 = new CRC32();
        crc32.update(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        return crc32.getValue();
    }

    private static void fileCompare(final File file1, final File file2) throws IOException {
        assertEquals(fileCRC32(file1), fileCRC32(file2));
    }

    private static void fileIsEmpty(final File file) throws IOException {
        assertEquals(0, Files.readAllLines(file.toPath()).size());
    }

    private static void dirCompare(final File dir1, final File dir2) throws IOException {
        assertTrue(dir1.isDirectory());
        assertTrue(dir2.isDirectory());

        final String[] childrens1 = dir1.list();
        final String[] childrens2 = dir2.list();
        assertEquals(childrens1.length, childrens2.length);

        for (int i = 0; i < childrens1.length; ++i) {
            //@formatter:off
            fileCompare(
                    new File(dir1.getAbsolutePath() + File.separatorChar + childrens1[i]),
                    new File(dir2.getAbsolutePath() + File.separatorChar + childrens2[i])
                );
            //@formatter:on
        }
    }

    private static void dirIsEmpty(final File file) throws IOException {
        assertEquals(0, file.list().length);
    }

    private static void dirDelete(final File file) throws IOException {
        if (file.isFile()) {
            file.delete();
        }
        if (file.isDirectory()) {
            for (String child : file.list()) {
                dirDelete(new File(file.getAbsolutePath() + File.separatorChar + child));
            }
            file.delete();
        }
    }

    /**
     * Test for {@link IOHelper#saveOutlines(File, File)}.
     * 
     * @throws IOException
     */
    @Test
    public void saveBookmarks() throws IOException {
        final String rootPath = TEST_PATH + File.separatorChar + "outlines";
        final String[] testFiles = new String[] { "bookmarks", "cmp_changed_bookmarks" };
        for (String filename : testFiles) {
            final String basePath = rootPath + File.separatorChar + filename;

            final File pdfFile = new File(basePath + ".pdf");
            final File bookmarksFile = new File(basePath + "_bookmarks.txt");
            final File tempBookmarksFile = new File(basePath + "_bookmarks_temp.txt");

            // Execute.
            IOHelper.saveOutlines(pdfFile, tempBookmarksFile);

            // Compare results.
            fileCompare(bookmarksFile, tempBookmarksFile);

            // Clean.
            tempBookmarksFile.delete();
        }
    }

    /**
     * Test for {@link IOHelper#updateOutlines(File, File)}.
     * 
     * @throws IOException
     */
    @Test
    public void updateBookmarks() throws IOException {
        final Path originalPdfPath = Paths.get(TEST_PATH, "document.pdf");
        final Path pdfPath = Paths.get(TEST_PATH, "outlines", "document.pdf");
        final Path originalOutlinesPath = Paths.get(TEST_PATH, "outlines", "bookmarks_bookmarks.txt");
        final Path outlinesPath = Paths.get(TEST_PATH, "outlines", "bookmarks_bookmarks_new.txt");

        // Copy original file.
        Files.copy(originalPdfPath, pdfPath, StandardCopyOption.REPLACE_EXISTING);

        // Save bookmarks from this file.
        IOHelper.saveOutlines(pdfPath.toFile(), outlinesPath.toFile());

        // Check original outlines.
        fileIsEmpty(outlinesPath.toFile());
        outlinesPath.toFile().delete();

        // Add bookmarks to new file.
        IOHelper.updateOutlines(pdfPath.toFile(), originalOutlinesPath.toFile());

        // Save bookmarks from this file.
        IOHelper.saveOutlines(pdfPath.toFile(), outlinesPath.toFile());

        // Check new file.
        fileCompare(originalOutlinesPath.toFile(), originalOutlinesPath.toFile());
        outlinesPath.toFile().delete();

        // Clean.
        pdfPath.toFile().delete();
    }

    /**
     * Test for {@link IOHelper#saveMetadata(File, File)}.
     * 
     * @throws IOException
     */
    @Test
    public void saveMetadata() throws IOException {
        final String rootPath = TEST_PATH + File.separatorChar + "metadata";
        final String[] testFiles = new String[] { "title-bar", "cmp_state_metadata" };
        for (String filename : testFiles) {
            final String basePath = rootPath + File.separatorChar + filename;

            final File pdfFile = new File(basePath + ".pdf");
            final File metadataFile = new File(basePath + "_metadata.txt");
            final File tempMetadataFile = new File(basePath + "_metadata_temp.txt");

            // Execute.
            IOHelper.saveMetadata(pdfFile, tempMetadataFile);

            // Compare results.
            fileCompare(metadataFile, tempMetadataFile);

            // Clean.
            tempMetadataFile.delete();
        }
    }

    /**
     * Test for {@link IOHelper#updateMetadata(File, File)}.
     * 
     * @throws IOException
     */
    @Test
    public void updateMetadata() throws IOException {
        // TODO
    }

    /**
     * Test for {@link IOHelper#saveAttachments(File, File)}.
     * 
     * @throws IOException
     */
    @Test
    public void saveAttachments() throws IOException {
        final String rootPath = TEST_PATH + File.separatorChar + "attachments";
        final String[] testFiles = new String[] { "cmp_hello_with_attachment", "cmp_hello_with_attachments" };
        for (String filename : testFiles) {
            final String basePath = rootPath + File.separatorChar + filename;

            final File pdfFile = new File(basePath + ".pdf");
            final File attachmentsFile = new File(basePath + "_files");

            final File tempAttachmentsFile = new File(basePath + "_files_temp");
            tempAttachmentsFile.mkdirs();
            assertEquals(0, tempAttachmentsFile.list().length);

            // Execute.
            IOHelper.saveAttachments(pdfFile, tempAttachmentsFile);

            // Compare results.
            dirCompare(attachmentsFile, tempAttachmentsFile);

            // Clean.
            dirDelete(tempAttachmentsFile);
        }
    }

    /**
     * Test for {@link IOHelper#removeAttachments(File)}.
     * 
     * @throws IOException
     */
    @Test
    public void removeAttachments() throws IOException {
        final String[] testFiles = new String[] { "cmp_hello_with_attachment", "cmp_hello_with_attachments" };
        for (String filename : testFiles) {
            final Path originalPdfPath = Paths.get(TEST_PATH, "attachments", filename + ".pdf");
            final Path pdfPath = Paths.get(TEST_PATH, "attachments", filename + "_copy.pdf");

            // Copy file.
            Files.copy(originalPdfPath, pdfPath, StandardCopyOption.REPLACE_EXISTING);

            final Path attachmentsDir = Paths.get(TEST_PATH, "attachments", filename);
            attachmentsDir.toFile().mkdirs();

            // Execute.
            IOHelper.removeAttachments(pdfPath.toFile());
            IOHelper.saveAttachments(pdfPath.toFile(), attachmentsDir.toFile());

            // Check that dir is empty.
            dirIsEmpty(attachmentsDir.toFile());
            dirDelete(attachmentsDir.toFile());

            // Clean.
            pdfPath.toFile().delete();
        }
    }

    /**
     * Test for {@link IOHelper#addAttachments(File, java.util.List)}.
     * 
     * @throws IOException
     */
    @Test
    public void addAttachments() throws IOException {
        // TODO
    }
}
