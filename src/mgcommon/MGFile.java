package mgcommon;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MGFile {

    public UUID id;

    public int createdBy;
    public Date createdOn;
    public int modifiedBy;
    public Date modifiedOn;
    public int accessedBy;
    public Date accessedOn;

    public String label;
    public String description;
    public String fileName;
    public String fileType;
    public String category;
    public int blockCount;
    public long size;
    public UUID dataSource;

    public Date timeStart;
    public Date timeEnd;
    public double gisX;
    public double gisY;

    public Set<UUID> linkedObject;
    public Map<UUID, UUID> property;



    public MGFile(UUID id) {
        this.id = id;
    }
    public MGFile() {
    }
}
