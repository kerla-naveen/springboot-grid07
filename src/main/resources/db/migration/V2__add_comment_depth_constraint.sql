-- Add depth constraint to prevent exceeding maximum depth
ALTER TABLE comment ADD CONSTRAINT check_comment_depth 
CHECK (depth_level <= 20);

-- Add foreign key constraint for parent comment
ALTER TABLE comment ADD CONSTRAINT fk_comment_parent 
FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE CASCADE;
