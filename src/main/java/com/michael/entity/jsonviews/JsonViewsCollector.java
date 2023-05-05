package com.michael.entity.jsonviews;

import javax.persistence.Entity;

public interface JsonViewsCollector {


    interface BaseEntity{
        interface OnlyOid{

        }
    }

    interface Entity{
        interface Default extends BaseEntity.OnlyOid {

        }
    }
    interface Users extends Entity{

        interface WithPassword extends Default{

        }

        interface WithAvatars extends Default{

        }
    }
}
