package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf;

public class PdfGenerationConfig {
    
    private String fileName;
    private String html;
    private Float zoom;
    private DesiredCapabilities desiredCapabilities;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Float getZoom() {
        return zoom;
    }

    public void setZoom(Float zoom) {
        this.zoom = zoom;
    }

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    static class DesiredCapabilities {
        private String engine;

        public DesiredCapabilities() {
            
        }
        
        public DesiredCapabilities(String engine) {
            this.engine = engine;
        }
        
        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }
    }
    
}
